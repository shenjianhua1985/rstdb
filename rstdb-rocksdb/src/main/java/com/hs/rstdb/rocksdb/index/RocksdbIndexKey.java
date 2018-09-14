package com.hs.rstdb.rocksdb.index;

import com.hs.rstdb.entry.CommonInfo;
import com.hs.rstdb.rocksdb.RocksDbConstant;
import com.hs.rstdb.serializable.ByteSerializationUtil;
import com.hs.rstdb.spi.IndexKeyMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从查询条件获取索引值
 * Created by sjh on 2018/9/7.
 */
public class RocksdbIndexKey implements IndexKeyMode {

    public Map<String, byte[]> getIndexsKey(CommonInfo info, Map<Object, Object> map) {

        Map<String, byte[]> reList = new HashMap<>();
        String prix = RocksDbConstant.DB_KEY.TABLE_PRVE + info.getTableName() + "_";
        Map<String, List<String>> mapIndex = info.getIndexs(); // 得到所有索引
        for (String indexName : mapIndex.keySet()) {
            List<String> list = mapIndex.get(indexName); // 索引字段
            StringBuffer sb = new StringBuffer(prix + indexName);
            for (String str : list) {
                Object ob = map.get(str);
                if (ob == null) {
                    return null;
                }
                sb.append("_" + ob.toString());
            }
            reList.put(indexName, ByteSerializationUtil.serializer(sb.toString()));
        }
        return reList;
    }
}
