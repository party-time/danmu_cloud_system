package cn.partytime.model;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/8/19.
 */
public class DanmuResult implements Serializable {


    private static final long serialVersionUID = 1783064395407395137L;
    /**
     * 弹幕类型
     */
    private String type;
    /**
     * 弹幕数据
     */
    private Object data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
