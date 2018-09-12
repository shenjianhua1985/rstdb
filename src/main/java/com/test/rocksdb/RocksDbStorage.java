package com.test.rocksdb;

import com.test.db.api.Storage;
import com.test.db.api.StorageEntry;
import com.test.entry.BaseEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * rocksdb 的存储实现
 * Created by sjh on 2018/9/5.
 */
public class RocksDbStorage implements Storage {
    private StorageEntry storageEntry;

    public RocksDbStorage(StorageEntry storageEntry){
        this.storageEntry = storageEntry;
    }

    public void addOrEdit(BaseEntry entry) {
        // 解析数据
        String name = storageEntry.getTableName(entry);
        Map<byte[],byte[]> map = storageEntry.getKeyAndValueForAdd(entry);

        // 存储数据
        RocksDbFactory.getRocksDbManager(name).batchPut(map);
    }

    public void del(BaseEntry entry) {
        String name = storageEntry.getTableName(entry);
        List<byte[]> keys = storageEntry.getKey(entry);
        RocksDbFactory.getRocksDbManager(name).batchDel(keys);
    }

    public List<BaseEntry> query(BaseEntry entry, Map<Object, Object> m) {
        List<byte[]> keys = storageEntry.getKeyForQuery(entry, m);
        String name = storageEntry.getTableName(entry);
        List<byte[]> values = RocksDbFactory.getRocksDbManager(name).batchGet(keys);
        if (values != null) {
            List<BaseEntry> list = new ArrayList<BaseEntry>();
            for (byte[] b : values) {
                BaseEntry be = storageEntry.change(b);
                list.add(be);
            }
            return list;
        }
        return null;
    }

}
