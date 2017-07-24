package cn.partytime.model.command;

/**
 * Created by lENOVO on 2016/11/24.
 */
public class DanmuComModel {

    /**弹幕编号*/
    private String id;

    /**弹幕颜色*/
    private String color;

    /**弹幕内容*/
    private String message;

    /**弹幕发送者openId*/
    private String openId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
