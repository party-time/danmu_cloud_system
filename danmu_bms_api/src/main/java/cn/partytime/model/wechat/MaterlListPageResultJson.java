package cn.partytime.model.wechat;

import java.util.List;

/**
 * Created by administrator on 2017/4/7.
 */
public class MaterlListPageResultJson {


    private Integer total_count;

    private Integer item_count;

    private List<MaterlResultJson> item;


    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Integer getItem_count() {
        return item_count;
    }

    public void setItem_count(Integer item_count) {
        this.item_count = item_count;
    }

    public List<MaterlResultJson> getItem() {
        return item;
    }

    public void setItem(List<MaterlResultJson> item) {
        this.item = item;
    }
}
