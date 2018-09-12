package com.test.rocksdb;

import com.test.db.api.IndexManage;
import com.test.rocksdb.serializable.RocksDbSerializationUtil;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.util.SizeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 索引处理类
 * Created by sjh on 2018/9/6.
 */
public class RocksDbIndexManager implements IndexManage {

    private static final Logger log = LoggerFactory.getLogger(RocksDbIndexManager.class);

    private String name;
    private RocksDB db;
    private String path = System.getProperty("user.dir") + File.separator + "index" + File.separator;
    private static Map<String, IndexManage> mapRocksDbIndexManager = new HashMap<>();


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

    public static IndexManage getInstance(String fileName) {
        IndexManage r = mapRocksDbIndexManager.get(fileName);
        if (r == null) {
            r = new RocksDbIndexManager(fileName);
            mapRocksDbIndexManager.put(fileName, r);
        }
        return r;
    }

    private RocksDbIndexManager(String fileName) {
        try {

            String data = path + fileName;
            File file = new File(data);
            if (!file.exists()) {
                file.mkdirs();
            }
            log.info("生成索引" + fileName + "路径为:{}", data);
            db = RocksDB.open(options, data);
        } catch (RocksDBException e) {
            log.error("", e);
        }
    }

    public Long getIndex(String indexKey) {
        try {
            byte[] in = db.get(RocksDbSerializationUtil.serializer(indexKey));
            if (in == null) {
                return 0L;
            }
            return Long.parseLong(RocksDbSerializationUtil.deserializer(in));
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public void updateIndex(String indexName, Long index) {
        try {
            db.put(RocksDbSerializationUtil.serializer(indexName), RocksDbSerializationUtil.serializer(String.valueOf(index)));
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
