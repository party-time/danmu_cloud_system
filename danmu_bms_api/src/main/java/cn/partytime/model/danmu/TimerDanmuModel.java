package cn.partytime.model.danmu;

import java.util.Map;

/**
 * Created by dm on 2017/5/22.
 */
public class TimerDanmuModel {

    private String id;

    /**活动编号*/
    private String partyId;


    private String typeName;

    /**特效id*/
    private String code;
    /**颜色*/
    private String color;

    /**时间*/
    private Integer beginTime;
    /**
     * 结束时间
     */
    private Integer endTime;

    /**
     * 弹幕内容
     */
    private Map<String,Object> content;

    /**
     * 显示内容
     */
    private Object msg;


    /**
     * 模板编号
     */
    private String templateId;

    /**弹幕类型*/
    private Integer danmuType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
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

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getDanmuType() {
        return danmuType;
    }

    public void setDanmuType(Integer danmuType) {
        this.danmuType = danmuType;
    }
}
