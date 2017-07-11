package cn.partytime.model;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/8/31.
 */
public class ProtocolModel<T> implements Serializable {


    private static final long serialVersionUID = 793345282313615131L;

    /**
     * 类型
     */
    private String type;

    /**
     * 唯一标识
     */
    private String code;

    /**
     * 消息来源:管理员：0；用户：1
     */
    private Integer messageSrc;

    /**
     * 客户端类型 设备：0；手机端：1  javaClient:2
     */
    private String clientType;

    /**
     * 屏幕编号
     */
    private Integer screenId;


    /**
     * 当前正在进行的活动
     */
    private String partyId;


    T data;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getScreenId() {
        return screenId;
    }

    public void setScreenId(Integer screenId) {
        this.screenId = screenId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Integer getMessageSrc() {
        return messageSrc;
    }

    public void setMessageSrc(Integer messageSrc) {
        this.messageSrc = messageSrc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
}
