package cn.partytime.model.danmu;

import java.util.Map;

/**
 * Created by dm on 2017/5/25.
 */
public class HistoryDanmuModel {

    private String id;

    /**
     * 弹幕颜色
     */
    private String color;

    /**
     * 弹幕内容
     */
    private Object msg;

    /**
     * 弹幕内容
     */
    private Map<String,Object> content;


    private Boolean isBlocked = false;
    //弹幕池
    private String danmuPoolId;

    private String checkUserId;

    /***弹幕来源 管理员:0,微信用户:1*/
    private int danmuSrc;

    /**0:非语音 1：语音弹幕*/
    private int type;

    /**
     * 模板编号
     */
    private String templateId;

    /**是否查看状态*/
    private boolean viewFlg=false;


    /**弹幕类型*/
    private Integer danmuType;




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

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public String getDanmuPoolId() {
        return danmuPoolId;
    }

    public void setDanmuPoolId(String danmuPoolId) {
        this.danmuPoolId = danmuPoolId;
    }

    public String getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(String checkUserId) {
        this.checkUserId = checkUserId;
    }

    public int getDanmuSrc() {
        return danmuSrc;
    }

    public void setDanmuSrc(int danmuSrc) {
        this.danmuSrc = danmuSrc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public boolean isViewFlg() {
        return viewFlg;
    }

    public void setViewFlg(boolean viewFlg) {
        this.viewFlg = viewFlg;
    }

    public Integer getDanmuType() {
        return danmuType;
    }

    public void setDanmuType(Integer danmuType) {
        this.danmuType = danmuType;
    }
}
