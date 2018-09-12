package com.test.rocksdb;

import com.test.db.api.IndexManage;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sjh on 2018/9/5.
 */
public class RocksDbManager{
    private static final Logger log = LoggerFactory.getLogger(RocksDbManager.class);

    private String name;
    private RocksDB db;
    private String path = System.getProperty("user.dir") + File.separator + "db" + File.separator;
    final WriteOptions writeOptions = new WriteOptions();
    private WriteBatch batch = new WriteBatch();
    ;
    final Options options = new Options()
            .setCreateIfMissing(true)
            .setWriteBufferSize(8 * SizeUnit.KB)
            .setMaxWriteBufferNumber(3)
            .setMaxBackgroundCompactions(10);

    static {
        try {
            RocksDB.loadLibrary();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public RocksDbManager(String name) {
        try {
            String data = path + name;
            File file = new File(data);
            if (!file.exists()) {
                file.mkdirs();
            }
            log.info("生成数据库" + name + "路径为:{}", data);
            db = RocksDB.open(options, data);
        } catch (RocksDBException e) {
            log.error("", e);
        }
    }

    public void put(byte[] key, byte[] value) {
        try {
            db.put(key, value);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void batchPut(Map<byte[],byte[]> map) {
        try {
            for (byte[] key:map.keySet()) {
                batch.put(key,map.get(key));
            }
            db.write(writeOptions,batch);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public List<byte[]> batchGet(List<byte[]> keys) {
        try {
            Map<byte[], byte[]> map = db.multiGet(keys);
            if (map != null && map.size() > 0) {
                List<byte[]> list = new ArrayList<byte[]>(map.size());
                for (byte[] key : map.keySet()) {
                    list.add(map.get(key));
                }
                return list;
            }

        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public byte[] get(byte[] key) {
        try {
            return db.get(key);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public void del(byte[] key) {
        try {
            db.delete(key);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void batchDel(List<byte[]> keys) {
        try {
            for (byte[] key : keys) {
                db.delete(key);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
