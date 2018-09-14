package com.hs.rstdb.spi;

import com.hs.rstdb.entry.CommonInfo;
import com.hs.rstdb.entry.DataObject;

/**
 * 根据类获取解析后的数据
 * Created by sjh on 2018/9/5.
 */
public interface Resolver {

    public CommonInfo analysis(DataObject baseEntry);
}
