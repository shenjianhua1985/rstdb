package com.test.rocksdb.index;

import com.test.entry.EntryInfo;
import com.test.rocksdb.RocksDbConstant;
import com.test.rocksdb.serializable.RocksDbSerializationUtil;

import java.util.List;
import java.util.Map;

/**
 * 从查询条件获取主键
 * Created by sjh on 2018/9/7.
 */
public class BaseFindPrimaryKey implements FindPrimaryKeyMode {

    /**
     * @param info
     * @param map
     * @return
     */
    public byte[] getKey(EntryInfo info, Map<Object, Object> map) {
        List<String> pKeys = info.getPrimaryKeyList(); // 获取所有的主键字段
        StringBuffer sb = new StringBuffer(RocksDbConstant.DB_KEY.TABLE_PRVE + info.getTableName());
        for (String key : pKeys) {
            Object ob = map.get(key);
            if (ob == null) {
                return null;
            }
            sb.append("_" + ob.toString());
        }
        return RocksDbSerializationUtil.serializer(sb.toString());
    }
}
