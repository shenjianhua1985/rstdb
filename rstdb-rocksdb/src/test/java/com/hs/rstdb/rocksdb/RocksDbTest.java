package com.hs.rstdb.rocksdb;

import com.hs.rstdb.entry.DataObject;
import com.hs.rstdb.entry.Pages;
import com.hs.rstdb.rocksdb.guice.InjectorContext;
import com.hs.rstdb.rocksdb.test.entry.TranContent;
import com.hs.rstdb.rocksdb.test.entry.TranHistory;
import com.hs.rstdb.rocksdb.test.entry.TranInfo;
import com.hs.rstdb.serializable.ByteSerializationUtil;
import com.hs.rstdb.spi.Decompose;
import com.hs.rstdb.spi.StorageAdapter;
import com.hs.rstdb.spi.impl.DefaultResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sjh on 2018/9/13.
 */
public class RocksDbTest {
    private StorageAdapter storage;

    @Before
    public void setUp() throws Exception {
        // 普通方式获取实例
       /* DefaultResolver resolve = new DefaultResolver();
        Decompose create = new RocksDbDecompose(resolve);
        storage = new RocksDbStorageAdapter(create);*/

       // guice方式获取实例
       storage = InjectorContext.get(StorageAdapter.class);
    }

    @Test
    public void testPriKeys() {
        try {
            TranInfo info = new TranInfo();
            info.setTranNumber(100L);
            info.setContent(new TranContent("abcd" + System.currentTimeMillis()));
            String s = info.getContent().getContent();
            System.out.println("storage content:" + s);

            // 存储数据
            storage.addOrEdit(info);

            // 查询数据
            Map<Object, Object> mapQuery = new HashMap<>();
            mapQuery.put("tranNumber", "100");
            List<DataObject> list = storage.query(new TranInfo(), mapQuery, null);

            for (DataObject be : list) {
                byte[] aa = be.getDatas();
                TranInfo in = ByteSerializationUtil.deserializer(aa, TranInfo.class);
                if (in != null && in.getContent() != null) {
                    System.out.println("query content:" + in.getContent().getContent());
                } else {
                    System.out.println("not query data！");
                }
                System.out.println("222");
            }
            System.out.println("1111");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testIndexs() {
        try {
            // 构造实体
            String account = "sjh5005";
            TranHistory tranHistory = new TranHistory();
            tranHistory.setAccount(account);
            tranHistory.setState("success");
            tranHistory.setTime(System.currentTimeMillis());
            tranHistory.setTranContent("A转账给B1001元");
            tranHistory.setTranNumber(500);

            System.out.println("save begin...........");
            long tranNumber = tranHistory.getTranNumber();
            String content = tranHistory.getTranContent();
            // 存储多条数据
            for (int i = 0; i < 5; i++) {
                tranHistory.setTranNumber(tranNumber + i);
                tranHistory.setTranContent(content + "-" + i);
                storage.addOrEdit(tranHistory);
            }
            System.out.println("save end...........");

            // 查询数据
            Map<Object, Object> mapQuery = new HashMap<>();
            mapQuery.put("account", account);
            Pages page = new Pages();
            page.setOffset(0);
            page.setLimit(10);

            System.out.println("query begin...........");
            List<DataObject> list = storage.query(new TranHistory(), mapQuery, page);
            for (DataObject be : list) {
                byte[] aa = be.getDatas();
                TranHistory in = ByteSerializationUtil.deserializer(aa, TranHistory.class);
                if (in != null) {
                    System.out.println("query tran history:" + in.toString());
                } else {
                    System.out.println("not query data！");
                }
            }
            System.out.println("query end...........");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}