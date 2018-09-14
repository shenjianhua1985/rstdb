package com.hs.rstdb.rocksdb;

/**
 * Created by sjh on 2018/9/6.
 */
public interface RocksDbConstant {

    interface DB_KEY {
        String TABLE_PRVE= "t_"; // 存储表数据时，key值的前缀
        String INDEX_PRVE= "i_"; // 存储索引数据时，key值的前缀
    }
}
