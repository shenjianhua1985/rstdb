package com.test.rocksdb.index;

import com.test.entry.EntryInfo;

import java.util.Map;

/**
 * Created by sjh on 2018/9/7.
 */
public interface FindPrimaryKeyMode {
    /**
     * 获取主键值
     * @param info
     * @param map
     * @return
     */
    public byte[] getKey(EntryInfo info, Map<Object, Object> map);
}
