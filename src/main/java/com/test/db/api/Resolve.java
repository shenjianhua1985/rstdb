package com.test.db.api;

import com.test.entry.BaseEntry;
import com.test.entry.EntryInfo;

/**
 * 根据类获取解析后的数据
 * Created by sjh on 2018/9/5.
 */
public interface Resolve {

    public EntryInfo analysis(BaseEntry baseEntry);
}
