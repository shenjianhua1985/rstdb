package com.test.db.api;

import com.test.annotation.IndexTag;
import com.test.annotation.KeyTag;
import com.test.annotation.TableName;
import com.test.entry.BaseEntry;
import com.test.entry.EntryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by sjh on 2018/9/5.
 */
public class BaseEntryResolve implements Resolve {
    private static final Logger log = LoggerFactory.getLogger(BaseEntryResolve.class);

    // 缓存解析出来的类信息
    private static Map<String, EntryInfo> mapCache = new HashMap<String, EntryInfo>();

    /**
     * 根据类解析出需要的字段
     *
     * @param baseEntry
     * @return
     */
    public EntryInfo analysis(BaseEntry baseEntry) {
        // 获取表名
        String tableName = getTableName(baseEntry);
        EntryInfo info = mapCache.get(tableName);
        if (info != null) {
            return info;
        } else {
            return handler(baseEntry);
        }

    }

    private String getTableName(BaseEntry baseEntry) {
        try {
            // 获取类上的注解
            TableName tn = baseEntry.getClass().getAnnotation(TableName.class);
            if (tn != null) {
                return tn.value();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    private EntryInfo handler(BaseEntry baseEntry) {
        EntryInfo info = new EntryInfo();
        try {
            // 获取类上的注解
            info.setTableName(getTableName(baseEntry));
            List<TempEntry> priKeyList = new ArrayList<TempEntry>(); // 主键只有一个

            Map<String, List<TempEntry>> indexs = new HashMap<String, List<TempEntry>>();

            // 获取字段上的注解
            Field[] field = baseEntry.getClass().getDeclaredFields();

            if (field != null) {
                for (Field fie : field) {
                    if (!fie.isAccessible()) {
                        fie.setAccessible(true);
                    }
                    Annotation[] ans = fie.getAnnotations();
                    if (ans != null) {
                        for (Annotation an : ans) {
                            if (KeyTag.class.getName().equals(an.annotationType().getName())) {
                                KeyTag kt = (KeyTag) an;
                                int i = kt.value();
                                TempEntry te = new TempEntry();
                                te.setName(fie.getName());
                                te.setIndex(i);
                                priKeyList.add(te);
                            }
                            if (IndexTag.class.getName().equals(an.annotationType().getName())) {
                                IndexTag kt = (IndexTag) an;
                                kt.name();// 索引名
                                kt.value();// 索引下标
                                fie.getName();// 字段名

                                TempEntry te = new TempEntry();
                                te.setName(fie.getName());
                                te.setIndex(kt.value());

                                List<TempEntry> list = indexs.get(kt.name());
                                if (list == null) {
                                    list = new ArrayList<TempEntry>();
                                    indexs.put(kt.name(), list);
                                }
                                list.add(te);
                            }

                        }
                    }
                }
            }

            // 解析主键
            if (priKeyList.size() == 0) { // 没有主键，报错
                throw new Exception("must exits a Primary key");
            }
            // 排序
            Collections.sort(priKeyList, new Comparator<TempEntry>() {
                public int compare(TempEntry o1, TempEntry o2) {
                    return o2.getIndex() - o1.getIndex();
                }
            });
            List<String> primaryKeyList = info.getPrimaryKeyList();
            for (TempEntry te : priKeyList) {
                primaryKeyList.add(te.getName());
            }
            Map<String, List<String>> mm = info.getIndexs();
            for (String key : indexs.keySet()) {
                List<TempEntry> ll = indexs.get(key);
                Collections.sort(ll, new Comparator<TempEntry>() {
                    public int compare(TempEntry o1, TempEntry o2) {
                        return o2.getIndex() - o1.getIndex();
                    }
                });
                List<String> tmp = new ArrayList<String>();
                for (TempEntry te : ll) {
                    tmp.add(te.getName());
                }
                mm.put(key, tmp);
            }

            // 缓存数据
            mapCache.put(info.getTableName(),info);
        } catch (Exception e) {
            log.error("", e);
        }
        return info;
    }

    /**
     * 根据字段名 获取该字段的值
     *
     * @param baseEntry
     * @param fie
     * @return
     */
    @Deprecated
    private Object getPriValue(BaseEntry baseEntry, Field fie) throws Exception {
        String fieldName = fie.getName();
        char c = fieldName.charAt(0);
        String s = "get" + String.valueOf(c).toUpperCase() + fieldName.substring(1);
        Method m = baseEntry.getClass().getMethod(s);
        return m.invoke(baseEntry);
    }
}

class TempEntry {
    int index; // 下标
    String name;// 字段名

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
