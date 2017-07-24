package cn.partytime.model;

import java.util.List;

/**
 * Created by lENOVO on 2016/11/7.
 */
public class PageResultModel<T> {
    private long total = 0;
    private List<T> rows;

    public PageResultModel() {
    }

    public PageResultModel(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
