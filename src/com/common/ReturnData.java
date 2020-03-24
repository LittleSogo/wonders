package com.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：zj
 * @date ：Created in 2020/3/16 9:51
 * @description：作为返回数据实体类，它有两个参数，一个表示数据集合，一个是数据总条数
 * @modified By：
 * @version: $
 */
public class ReturnData<T> {
    private int total;

    private List<T> rows = new ArrayList<T>();

    public int getTotal() {
        return total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
