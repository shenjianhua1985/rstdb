package com.test;

import com.test.db.api.BaseEntryResolve;
import com.test.db.api.Storage;
import com.test.db.api.StorageEntry;
import com.test.entry.*;
import com.test.rocksdb.RocksDbDataCreate;
import com.test.rocksdb.RocksDbStorage;
import com.test.rocksdb.serializable.RocksDbSerializationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sjh on 2018/9/11.
 */
public class TestMain {

    public static void main(String[] args) {
        //testPriKeys();
        testIndexs();
    }

    /**
     * 测试按照索引保存和查询
     */
    public static void testIndexs() {
        try {
            // 构造实体
            String account = "sjh5005";
            TranHistory tranHistory = new TranHistory();
            tranHistory.setAccount(account);
            tranHistory.setState("success");
            tranHistory.setTime(System.currentTimeMillis());
            tranHistory.setTranContent("A转账给B1001元");
            tranHistory.setTranNumber(500);

            //
            BaseEntryResolve resolve = new BaseEntryResolve();
            StorageEntry create = new RocksDbDataCreate(resolve);
            Storage storage = new RocksDbStorage(create);
            System.out.println("save begin...........");
            long tranNumber = tranHistory.getTranNumber();
            String content = tranHistory.getTranContent();
            // 存储多条数据
            for (int i = 0 ; i < 5 ; i ++) {
                tranHistory.setTranNumber(tranNumber+ i);
                tranHistory.setTranContent(content + "-" + i);
                storage.addOrEdit(tranHistory);
            }
            System.out.println("save end...........");

            // 查询数据
            Map<Object, Object> mapQuery = new HashMap<>();
            mapQuery.put("account", account);
            Pages page =  new Pages();
            page.setOffset(0);
            page.setLimit(10);
            mapQuery.put(Pages.PAGES_KEY,page);

            System.out.println("query begin...........");
            List<BaseEntry> list = storage.query(new TranHistory(), mapQuery);
            for (BaseEntry be : list) {
                byte[] aa = be.getDatas();
                TranHistory in = RocksDbSerializationUtil.deserializer(aa, TranHistory.class);
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

    /**
     * 测试按照主键保存和查询
     */
    public static void testPriKeys() {
        try {
            BaseEntryResolve resolve = new BaseEntryResolve();
            StorageEntry create = new RocksDbDataCreate(resolve);
            Storage storage = new RocksDbStorage(create);

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
            List<BaseEntry> list = storage.query(new TranInfo(), mapQuery);

            for (BaseEntry be : list) {
                byte[] aa = be.getDatas();
                TranInfo in = RocksDbSerializationUtil.deserializer(aa, TranInfo.class);
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

}
