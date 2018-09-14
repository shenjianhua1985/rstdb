package com.hs.rstdb.spi;

import com.hs.rstdb.entry.CommonInfo;

import java.util.Map;

/**
 * Created by sjh on 2018/9/13.
 */
public interface IndexKeyMode {

    public Map<String,byte[]> getIndexsKey(CommonInfo info, Map<Object, Object> map);
}
