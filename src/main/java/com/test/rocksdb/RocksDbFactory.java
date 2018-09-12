package com.test.rocksdb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjh on 2018/9/5.
 */
public class RocksDbFactory {
    private static Map<String, RocksDbManager> map = new HashMap<String, RocksDbManager>();

    public static RocksDbManager getRocksDbManager(String name) {
        RocksDbManager db = map.get(name);
        if (db == null) {
            db = new RocksDbManager(name);
            map.put(name, db);
        }
        return db;
    }

}
