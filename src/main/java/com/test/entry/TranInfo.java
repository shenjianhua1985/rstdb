package com.test.entry;

import com.test.annotation.KeyTag;
import com.test.annotation.TableName;

/**
 * Created by sjh on 2018/9/5.
 */
@TableName("tran_info") // 存储的表名
public class TranInfo extends BaseEntry {

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
