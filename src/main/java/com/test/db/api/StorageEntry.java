package com.test.db.api;

import com.test.entry.BaseEntry;

import java.util.List;
import java.util.Map;

/**
 * 存储对象接口
 * Created by sjh on 2018/9/5.
 */
public interface StorageEntry {

    /**
     * 存储时使用的名称,使用表名
     *
     * @param entry
     * @return
     */
    public String getTableName(BaseEntry entry);

    /**
     * 为增加操作，从实例中获取key和value
     * @param entry
     * @return
     */
    public Map<byte[],byte[]> getKeyAndValueForAdd(BaseEntry entry);

    /**
     * 根据对象获取key，包含数据key和索引key
     *
     * @param entry
     * @return
     */
    public List<byte[]> getKey(BaseEntry entry);

    /**
     * 根据map中的内容获取key值
     *
     * @param entry
     * @return
     */
    public List<byte[]> getKeyForQuery(BaseEntry entry, Map<Object, Object> map);

    /**
     * 转成对象
     *
     * @return
     */
    public BaseEntry change(byte[] b);
}
