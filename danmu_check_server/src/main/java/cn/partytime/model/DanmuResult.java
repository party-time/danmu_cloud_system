package cn.partytime.model;

import java.io.Serializable;

public class DanmuResult implements Serializable {


    private static final long serialVersionUID = 1783064395407395137L;
    /**
     * 弹幕类型
     */
    private String type;

    private String name;

    private String status;

    private String color;

    private String msg;

    private String id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
