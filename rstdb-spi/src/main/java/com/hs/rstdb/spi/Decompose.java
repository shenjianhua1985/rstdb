package com.hs.rstdb.spi;

import com.hs.rstdb.entry.DataObject;
import com.hs.rstdb.entry.Pages;

import java.util.List;
import java.util.Map;

/**
 * 解析对象获取需要的key和value
 * Created by sjh on 2018/9/5.
 */
public interface Decompose {

    /**
     * 存储时使用的名称,使用表名
     *
     * @param cla 对象class
     * @return
     */
    public String getTableName(Class cla);

    /**
     * 从实例中获取key和value
     *
     * @param entry 实例对象
     * @return
     */
    public Map<byte[], byte[]> pickKeyValueMap(DataObject entry);

    /**
     * 根据对象获取key，包含数据key和索引key
     *
     * @param entry
     * @return
     */
    public List<byte[]> pickKeys(DataObject entry);

    /**
     * 根据查询中的参数获取key值
     *
     * @param entry
     * @param params
     * @return
     */
    public List<byte[]> pickKeyFromParams(DataObject entry, Map<Object, Object> params,Pages pages);

    /**
     * 转成对象
     *
     * @return
     */
    public DataObject change(byte[] b);
}
