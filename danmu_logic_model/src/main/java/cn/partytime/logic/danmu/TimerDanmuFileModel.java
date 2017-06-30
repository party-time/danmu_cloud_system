package cn.partytime.logic.danmu;

import cn.partytime.logic.danmu.ProtocolDanmuModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lENOVO on 2016/11/28.
 */
public class TimerDanmuFileModel implements Serializable {


    private static final long serialVersionUID = -2762832861681483053L;

    /**
     * 弹幕编号
     */
    private String id;

    /**弹幕类型*/
    private String type;


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

    /**
     * 时间
     */
    private Integer beginTime ;

    /**
     * 结束时间
     */
    private Integer endTime;

    /**开始：0；结束：1*/
    private int position;


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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Integer getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Integer beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }
}
