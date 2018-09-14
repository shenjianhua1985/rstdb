package com.hs.rstdb.rocksdb;

import com.google.inject.Inject;
import com.hs.rstdb.entry.DataObject;
import com.hs.rstdb.entry.Pages;
import com.hs.rstdb.spi.Decompose;
import com.hs.rstdb.spi.StorageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * rocksdb 的存储实现
 * Created by sjh on 2018/9/5.
 */
public class RocksDbStorageAdapter implements StorageAdapter {

    private Decompose storageEntry;

    @Inject
    public RocksDbStorageAdapter(Decompose storageEntry) {
        this.storageEntry = storageEntry;
    }

    public void addOrEdit(DataObject entry) {
        // 解析数据
        String name = storageEntry.getTableName(entry.getClass());
        Map<byte[], byte[]> map = storageEntry.pickKeyValueMap(entry);

        // 存储数据
        RocksDbFactory.getRocksDbManager(name).batchPut(map);
    }

    public void del(DataObject entry) {
        String name = storageEntry.getTableName(entry.getClass());
        List<byte[]> keys = storageEntry.pickKeys(entry);
        RocksDbFactory.getRocksDbManager(name).batchDel(keys);
    }

    public List<DataObject> query(DataObject entry, Map<Object, Object> m, Pages pages) {
        List<byte[]> keys = storageEntry.pickKeyFromParams(entry, m,pages);
        String name = storageEntry.getTableName(entry.getClass());
        List<byte[]> values = RocksDbFactory.getRocksDbManager(name).batchGet(keys);
        if (values != null) {
            List<DataObject> list = new ArrayList<DataObject>();
            for (byte[] b : values) {
                DataObject be = storageEntry.change(b);
                list.add(be);
            }
            return list;
        }
        return null;
    }

}
