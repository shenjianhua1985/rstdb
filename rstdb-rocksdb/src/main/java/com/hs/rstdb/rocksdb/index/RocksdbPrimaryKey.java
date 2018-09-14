package com.hs.rstdb.rocksdb.index;

import com.hs.rstdb.entry.CommonInfo;
import com.hs.rstdb.rocksdb.RocksDbConstant;
import com.hs.rstdb.serializable.ByteSerializationUtil;
import com.hs.rstdb.spi.PrimaryKeyMode;

import java.util.List;
import java.util.Map;

/**
 * 从查询条件获取主键
 * Created by sjh on 2018/9/7.
 */
public class RocksdbPrimaryKey implements PrimaryKeyMode {

    /**
     * @param info
     * @param map
     * @return
     */
    public byte[] getKey(CommonInfo info, Map<Object, Object> map) {
        List<String> pKeys = info.getPrimaryKeyList(); // 获取所有的主键字段
        StringBuffer sb = new StringBuffer(RocksDbConstant.DB_KEY.TABLE_PRVE + info.getTableName());
        for (String key : pKeys) {
            Object ob = map.get(key);
            if (ob == null) {
                return null;
            }
            sb.append("_" + ob.toString());
        }
        return ByteSerializationUtil.serializer(sb.toString());
    }
}
