package com.hs.rstdb.rocksdb.test.entry;

import com.hs.rstdb.annotation.KeyTag;
import com.hs.rstdb.annotation.TableName;
import com.hs.rstdb.entry.DataObject;

/**
 * Created by sjh on 2018/9/5.
 */
@TableName("tran_info") // 存储的表名
public class TranInfo extends DataObject {

    @KeyTag(1) // 存储时的key，多个key 拼装
    private Long tranNumber;

    // 存储时的value，多个value拼接
    private TranContent content;

    public Long getTranNumber() {
        return tranNumber;
    }

    public void setTranNumber(Long tranNumber) {
        this.tranNumber = tranNumber;
    }

    public TranContent getContent() {
        return content;
    }

    public void setContent(TranContent content) {
        this.content = content;
    }
}
