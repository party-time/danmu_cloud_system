package cn.partytime.logic.danmu;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/11/24.
 */
public class ProtocolDanmuModel  implements Serializable{


    private static final long serialVersionUID = -2762832861681483053L;

    /**
     * 弹幕编号
     */
    private String id;

    /**弹幕类型*/
    private String type;

    /**
     * 弹幕名称
     */
    private String name;

    /**
     * 弹幕颜色
     */
    private String color;

    /**
     * 名称后缀
     */
    private String suffix;

    /**
     * 弹幕内容
     */
    private String message;


    private Integer status;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
