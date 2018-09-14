package com.hs.rstdb.rocksdb.test.entry;

/**
 * Created by sjh on 2018/9/5.
 */
public class TranContent {

    public TranContent(String content) {
        this.content = content;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
