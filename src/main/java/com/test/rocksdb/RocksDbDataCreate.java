package com.test.rocksdb;

import com.test.db.api.Resolve;
import com.test.db.api.StorageEntry;
import com.test.entry.BaseEntry;
import com.test.entry.EntryInfo;
import com.test.entry.Pages;
import com.test.rocksdb.index.BaseFindIndexKey;
import com.test.rocksdb.index.BaseFindPrimaryKey;
import com.test.rocksdb.index.FindIndexKeyMode;
import com.test.rocksdb.index.FindPrimaryKeyMode;
import com.test.rocksdb.serializable.RocksDbSerializationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建key和value数据
 * Created by sjh on 2018/9/5.
 */
public class RocksDbDataCreate implements StorageEntry {
    private static final Logger log = LoggerFactory.getLogger(RocksDbDataCreate.class);

    private Resolve resolve;

    public RocksDbDataCreate(Resolve resolve) {
        this.resolve = resolve;
    }

    private EntryInfo change(BaseEntry entry) {
        return this.resolve.analysis(entry);
    }

    public String getTableName(BaseEntry entry) {
        EntryInfo info = resolve.analysis(entry);
        if (info != null) {
            return info.getTableName();
        }
        return null;
    }

    /**
     * 获取所有待存储的key和value数据，包括需要存储的索引信息
     *
     * @param entry
     * @return
     */
    public Map<byte[], byte[]> getKeyAndValueForAdd(BaseEntry entry) {
        return getKeyAndValue(entry, true);
    }

    /**
     * @param entry
     * @param needValue 是否需要value
     * @return
     */
    private Map<byte[], byte[]> getKeyAndValue(BaseEntry entry, boolean needValue) {
        EntryInfo info = resolve.analysis(entry);
        Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
        if (info != null) {
            List<String> keys = info.getPrimaryKeyList(); // 索取所有的主键
            if (keys == null || keys.size() == 0) {
                return null;
            }
            StringBuffer keySb = new StringBuffer();
            for (String key : keys) {
                String k = getValueForField(entry, key);
                if (k == null) {
                    return null;
                }
                keySb.append("_" + k);
            }
            // 主键的key
            String key = RocksDbConstant.DB_KEY.TABLE_PRVE + info.getTableName() + keySb.toString();
            // 主键key对应需要存储的数据entry，需要序列化
            byte[] keyByte = RocksDbSerializationUtil.serializer(key);
            byte[] valueByte = null;
            if (needValue) {
                valueByte = RocksDbSerializationUtil.serializer(entry);
            }
            map.put(keyByte, valueByte);

            // 存储索引,可能存在多个索引，增加之前要查询当前的索引值
            Map<String, List<String>> mapIndex = info.getIndexs();
            if (mapIndex != null && mapIndex.size() > 0) {
                for (String str : mapIndex.keySet()) {
                    // str 索引名
                    List<String> list = mapIndex.get(str); // 索引字段

                    StringBuffer keyTmp = new StringBuffer(RocksDbConstant.DB_KEY.TABLE_PRVE + info.getTableName() + "_" + str);
                    for (String s : list) {
                        String k = getValueForField(entry, s);
                        if (k == null) {
                            return null;
                        }
                        keyTmp.append("_" + k);
                    }
                    String indexKey = keyTmp.toString();
                    // 查询数据获取最新的 索引值
                    Long indexLong = RocksDbIndexManager.getInstance(str).getIndex(indexKey);
                    if (indexLong == null) {
                        indexLong = 0L;
                    } else {
                        indexLong = indexLong + 1;
                    }
                    // 更新当前索引
                    RocksDbIndexManager.getInstance(str).updateIndex(indexKey,indexLong);
                    // 保存索引对应的数据
                    map.put(RocksDbSerializationUtil.serializer(indexKey + "_" + indexLong), valueByte);
                }
            }
        }
        return map;
    }

    public List<byte[]> getKey(BaseEntry entry) {
        Map<byte[], byte[]> map = getKeyAndValue(entry, false);
        if (map == null || map.size() == 0) {
            return null;
        }
        List<byte[]> reList = new ArrayList<byte[]>();
        for (byte[] b : map.keySet()) {
            reList.add(b);
        }
        return reList;
    }

    public List<byte[]> getKeyForQuery(BaseEntry entry, Map<Object, Object> map) {
        List<byte[]> reList = new ArrayList<byte[]>();
        // 查询条件是主键字段 -- 只能查询出一条记录
        EntryInfo info = resolve.analysis(entry);
        boolean queryByPrimaryKey = true; // 是否按照主键查询
        for (String ky : info.getPrimaryKeyList()) { // 如果主键都在查询条件中
            if (!map.keySet().contains(ky)) {
                queryByPrimaryKey = false; // 不是按照主键查询
                break;
            }
        }

        if (queryByPrimaryKey) {
            FindPrimaryKeyMode findPrimaryKeyMode = new BaseFindPrimaryKey();
            byte[] b = findPrimaryKeyMode.getKey(info, map);
            if (b != null) {
                reList.add(b);
            }
        }

        if (info.getIndexs().size() > 0) { //按照索引查询,一次查询只能按照一个索引
            FindIndexKeyMode findIndexKeyMode = new BaseFindIndexKey();
            // 获取存储索引号的key
            Map<String, byte[]> mapB = findIndexKeyMode.getIndexsKey(info, map);
            // 根据key查询存储的数据
            if (mapB != null && mapB.size() > 0) {
                for (String indexName : mapB.keySet()) {
                    byte[] tmpKey = mapB.get(indexName);
                    String s = RocksDbSerializationUtil.deserializer(tmpKey);
                    Long in = RocksDbIndexManager.getInstance(indexName).getIndex(s);
                    // 查询多条记录,根据map中的分页条件查询。
                    Pages page = (Pages)map.get(Pages.PAGES_KEY);
                    if (page != null) {
                        // 根据请求返回多条记录
                        int limit = page.getLimit();
                        int offset = page.getOffset();
                        long begin = in - offset;
                        long end = begin - limit;
                        if (begin >= 0) {
                            for (long i = begin ; i >= end && i >= 0; i -- ) {
                                reList.add(RocksDbSerializationUtil.serializer(s + "_" + i));
                            }
                        }
                    } else { // 只查询一条
                        if (in != null) {
                            // 获取 t_表名_索引名_索引字段值_索引编号
                            reList.add(RocksDbSerializationUtil.serializer(s + "_" + in));
                        }
                    }
                }
            }
        }
        return reList;
    }

    public BaseEntry change(byte[] b) {
        BaseEntry en = RocksDbSerializationUtil.deserializer(b, BaseEntry.class);
        en.setDatas(b);
        return en;
    }

    /**
     * 通过字段名，得到实例中的字段值
     *
     * @param entry     实例
     * @param fieldName 字段名
     * @return 字段值
     */
    private String getValueForField(BaseEntry entry, String fieldName) {
        String methodName = this.getGetMethodName(fieldName);
        try {
            Object ob = entry.getClass().getMethod(methodName).invoke(entry);
            return ob == null ? null : ob.toString();
        } catch (IllegalAccessException e) {
            log.error("", e);
        } catch (InvocationTargetException e) {
            log.error("", e);
        } catch (NoSuchMethodException e) {
            log.error("", e);
        }
        return null;
    }

    private String getGetMethodName(String fieldName) {
        char c = fieldName.charAt(0);
        return "get" + String.valueOf(c).toUpperCase() + fieldName.substring(1);
    }

}

