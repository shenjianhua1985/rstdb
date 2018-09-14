package com.hs.rstdb.spi.impl;

import com.hs.rstdb.annotation.IndexTag;
import com.hs.rstdb.annotation.KeyTag;
import com.hs.rstdb.annotation.TableName;
import com.hs.rstdb.entry.CommonInfo;
import com.hs.rstdb.entry.DataObject;
import com.hs.rstdb.spi.Resolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by sjh on 2018/9/5.
 */
public class DefaultResolver implements Resolver {
    private static final Logger log = LoggerFactory.getLogger(DefaultResolver.class);

    // 缓存解析出来的类信息
    private static Map<String, CommonInfo> mapCache = new HashMap<String, CommonInfo>();

    /**
     * 根据类解析出需要的字段
     *
     * @param baseEntry
     * @return
     */
    public CommonInfo analysis(DataObject baseEntry) {
        // 获取表名
        String tableName = getTableName(baseEntry);
        CommonInfo info = mapCache.get(tableName);
        if (info != null) {
            return info;
        } else {
            return handler(baseEntry);
        }

    }

    private String getTableName(DataObject baseEntry) {
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

    private CommonInfo handler(DataObject baseEntry) {
        CommonInfo info = new CommonInfo();
        try {
            // 获取类上的注解
            info.setTableName(getTableName(baseEntry));
            List<InnerEntry> priKeyList = new ArrayList<InnerEntry>(); // 主键只有一个

            Map<String, List<InnerEntry>> indexs = new HashMap<String, List<InnerEntry>>();

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
                                InnerEntry te = new InnerEntry();
                                te.setName(fie.getName());
                                te.setIndex(i);
                                priKeyList.add(te);
                            }
                            if (IndexTag.class.getName().equals(an.annotationType().getName())) {
                                IndexTag kt = (IndexTag) an;
                                kt.name();// 索引名
                                kt.value();// 索引下标
                                fie.getName();// 字段名

                                InnerEntry te = new InnerEntry();
                                te.setName(fie.getName());
                                te.setIndex(kt.value());

                                List<InnerEntry> list = indexs.get(kt.name());
                                if (list == null) {
                                    list = new ArrayList<InnerEntry>();
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
            Collections.sort(priKeyList, new Comparator<InnerEntry>() {
                public int compare(InnerEntry o1, InnerEntry o2) {
                    return o2.getIndex() - o1.getIndex();
                }
            });
            List<String> primaryKeyList = info.getPrimaryKeyList();
            for (InnerEntry te : priKeyList) {
                primaryKeyList.add(te.getName());
            }
            Map<String, List<String>> mm = info.getIndexs();
            for (String key : indexs.keySet()) {
                List<InnerEntry> ll = indexs.get(key);
                Collections.sort(ll, new Comparator<InnerEntry>() {
                    public int compare(InnerEntry o1, InnerEntry o2) {
                        return o2.getIndex() - o1.getIndex();
                    }
                });
                List<String> tmp = new ArrayList<String>();
                for (InnerEntry te : ll) {
                    tmp.add(te.getName());
                }
                mm.put(key, tmp);
            }

            // 缓存数据
            mapCache.put(info.getTableName(), info);
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
    private Object getPriValue(DataObject baseEntry, Field fie) throws Exception {
        String fieldName = fie.getName();
        char c = fieldName.charAt(0);
        String s = "get" + String.valueOf(c).toUpperCase() + fieldName.substring(1);
        Method m = baseEntry.getClass().getMethod(s);
        return m.invoke(baseEntry);
    }

    private class InnerEntry {
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
}
