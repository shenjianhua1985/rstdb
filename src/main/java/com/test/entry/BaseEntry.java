package com.test.entry;

import com.test.rocksdb.serializable.RocksDbSerializationUtil;

/**
 * Created by sjh on 2018/9/5.
 */
public class BaseEntry {
    private byte[] datas;

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }

    public byte[] getDatas() {
        return datas;
    }
}
