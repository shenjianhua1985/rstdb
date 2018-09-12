package com.test.entry;

import com.test.annotation.IndexTag;
import com.test.annotation.KeyTag;
import com.test.annotation.TableName;

/**
 * Created by sjh on 2018/9/12.
 */
@TableName("tran_history") // 存储的表名
public class TranHistory extends BaseEntry {
    private long time; // 交易时间
    private String state; // 交易结果
    private String tranContent;

    @KeyTag(1)
    private long tranNumber;// 交易编号

    @IndexTag(name="index_tran_history_account",value = 1)
    private String account; // 交易账号

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getTranNumber() {
        return tranNumber;
    }

    public void setTranNumber(long tranNumber) {
        this.tranNumber = tranNumber;
    }

    public String getTranContent() {
        return tranContent;
    }

    public void setTranContent(String tranContent) {
        this.tranContent = tranContent;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "TranHistory{" +
                "time=" + time +
                ", state='" + state + '\'' +
                ", tranNumber=" + tranNumber +
                ", tranContent='" + tranContent + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
