package com.test.db.api;

/**
 * Created by sjh on 2018/9/6.
 */
public interface IndexManage {

    /**
     * 获取当前索引值
     * @return
     */
    public Long getIndex(String indexKey);

    /**
     * 更新索引
     * @param index
     */
    public void updateIndex(String indexKey, Long index);
}
