package com.test.entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对注解解析后的数据
 * Created by sjh on 2018/9/5.
 */
public class EntryInfo {
    private String tableName;
    private List<String> primaryKeyList = new ArrayList<String>(0); // 主键字段名
    private Map<String, List<String>> indexs = new HashMap<String, List<String>>(0); // 需要建立的索引名和对应的索引字段

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getPrimaryKeyList() {
        return primaryKeyList;
    }

    public void setPrimaryKeyList(List<String> primaryKeyList) {
        this.primaryKeyList = primaryKeyList;
    }

    public Map<String, List<String>> getIndexs() {
        return indexs;
    }

    public void setIndexs(Map<String, List<String>> indexs) {
        this.indexs = indexs;
    }
}
