package com.hs.rstdb.spi;

/**
 * index interface
 * Created by sjh on 2018/9/6.
 */
public interface IndexManager {

    /**
     * get newest index value
     *
     * @return
     */
    public Long getIndex(String indexKey);

    /**
     * update index value
     *
     * @param indexValue
     */
    public void updateIndex(String indexKey, Long indexValue);
}
