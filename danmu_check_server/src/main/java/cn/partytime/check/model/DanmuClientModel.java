package cn.partytime.check.model;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/9/1.
 */
public class DanmuClientModel implements Serializable {
    private static final long serialVersionUID = 7757607862683701760L;

    private String id;
    /*客户端名称*/
    private String name;
    /**
     * 注册码
     */
    private String registCode;

    /**
     * 客户端类型 设备：0；手机端：1
     */
    private int clientType;
    /**
     * 地址编号
     */
    private String addressId;

    /**
     * 活动编号
     */
    private String partyId;


    /**屏幕编号*/
    private int screenId;

    /**
     * 由客户端生成的唯一标识
     */
    private String danmuClientCode;

    /**
     *屏幕弹幕数
     */
    private Integer danmuCount;

    private Long lastTime;

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

    public String getRegistCode() {
        return registCode;
    }

    public void setRegistCode(String registCode) {
        this.registCode = registCode;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public String getDanmuClientCode() {
        return danmuClientCode;
    }

    public void setDanmuClientCode(String danmuClientCode) {
        this.danmuClientCode = danmuClientCode;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public Integer getDanmuCount() {
        return danmuCount;
    }

    public void setDanmuCount(Integer danmuCount) {
        this.danmuCount = danmuCount;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
}
