package com.hs.rstdb.entry;

/**
 * 分页
 * 返回结果不提供总数量
 * Created by sjh on 2018/9/12.
 */
public class Pages {

    private int offset;
    private int limit;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
