package com.hs.rstdb.spi;

import com.hs.rstdb.entry.CommonInfo;

import java.util.Map;

/**
 * Created by sjh on 2018/9/13.
 */
public interface PrimaryKeyMode {

    /**
     * 获取主键值
     *
     * @param info
     * @param params 参数
     * @return
     */
    public byte[] getKey(CommonInfo info, Map<Object, Object> params);
}
