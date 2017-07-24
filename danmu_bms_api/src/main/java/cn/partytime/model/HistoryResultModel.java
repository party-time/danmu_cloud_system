package cn.partytime.model;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/12/7.
 */
public class HistoryResultModel implements Serializable {

    private String type;

    private DanmuResultModel data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DanmuResultModel getData() {
        return data;
    }

    public void setData(DanmuResultModel data) {
        this.data = data;
    }
}
