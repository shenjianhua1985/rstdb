package com.hs.rstdb.entry;

/**
 * storage obejct parent class
 * Created by sjh on 2018/9/5.
 */
public class DataObject {
    private byte[] datas;

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }

    public byte[] getDatas() {
        return datas;
    }
}
