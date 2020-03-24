package com.common;

/**
 * @author ：zj
 * @date ：Created in 2020/3/16 9:56
 * @description：封装一个Page工具类
 * @modified By：
 * @version: $
 */
public class Page {
    private int pageNumber; //每页的条数

    private int offset; //数据库查询索引

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
