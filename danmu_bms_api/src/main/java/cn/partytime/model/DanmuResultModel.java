package cn.partytime.model;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/12/7.
 */
public class DanmuResultModel implements Serializable {

    private String color;

    private String message;

    private int beginTime;

    private String type;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }
}
