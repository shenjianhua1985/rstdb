package com.test.db.api;

import com.test.entry.BaseEntry;

import java.util.List;
import java.util.Map;

/**
 * 存储接口
 * Created by sjh on 2018/9/5.
 */
public interface Storage {

    /**
     * 增加获取查询
     *
     * @param entry
     */
    public void addOrEdit(BaseEntry entry);

    /**
     * 删除
     *
     * @param entry
     */
    public void del(BaseEntry entry);

    /**
     * 查询
     *
     * @param entry
     * @param map
     * @return
     */
    public List<BaseEntry> query(BaseEntry entry, Map<Object, Object> map);
}
