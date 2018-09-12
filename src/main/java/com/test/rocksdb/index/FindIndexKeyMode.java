package com.test.rocksdb.index;

import com.test.entry.EntryInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by sjh on 2018/9/7.
 */
public interface FindIndexKeyMode {
    /**
     * 获取索引key值
     * @param info
     * @param map
     * @return
     */
    public Map<String,byte[]> getIndexsKey(EntryInfo info, Map<Object, Object> map);
}
