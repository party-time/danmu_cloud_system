package cn.partytime.model;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/12/7.
 */
public class PotocolTimerDanmu implements Serializable {


    /**编号**/
    private String id;
    /**
     * 类型2
     */
    private String secondType;

    /***弹幕类型 0:弹幕；1:动漫；2：图片；3：闪光字；4：表情*/
    private String type;

    private String typeName;

    /**特效id*/
    private String code;
    /**颜色*/
    private String color;

    /**
     * 视频状态
     */
    private Integer status;

    /**内容*/
    private String message;

    /**时间*/
    private Integer beginTime;


    /**
     * 视频持续时间
     */
    private Integer lastTime;

    /**
     * 结束时间
     */
    private Integer endTime;


    /**
     * 弹幕方位
     */
    private Integer direction;


    /**
     * 名称后缀
     */
    private String suffix;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecondType() {
        return secondType;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLastTime() {
        return lastTime;
    }

    public void setLastTime(Integer lastTime) {
        this.lastTime = lastTime;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }
}
