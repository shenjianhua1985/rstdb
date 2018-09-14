package com.hs.rstdb.spi;

import com.hs.rstdb.entry.DataObject;
import com.hs.rstdb.entry.Pages;

import java.util.List;
import java.util.Map;

/**
 * 存储适配器
 * Created by sjh on 2018/9/5.
 */
public interface StorageAdapter {

    /**
     * 增加获取查询
     *
     * @param entry
     */
    public void addOrEdit(DataObject entry);

    /**
     * 删除
     *
     * @param entry
     */
    public void del(DataObject entry);

    /**
     * 查询
     *
     * @param entry 对象数据
     * @param params 参数信息，必须包含主键或者索引字段
     * @param pages 分页信息，可以为空
     * @return
     */
    public List<DataObject> query(DataObject entry, Map<Object, Object> params, Pages pages);
}
