package cn.partytime.logic.danmu;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/28.
 */
public class PartyLogicModel implements Serializable {

    private String partyId;

    private String partyName;

    private String addressId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 电影开始时间
     */
    private Date activeTime;

    /**
     * 活动状态
     */
    private int status;

    /**
     * 获取类型 0:活动场：1：弹幕场
     */
    private int type;


    /**
     * 场地类型 0 固定场地  1 临时场地
     */
    private int addressType;



    private String shortName;

    /**
     * H5模版的id
     */
    private String h5TempId;

    /**
     * 弹幕密度
     */
    private Integer dmDensity;



    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getH5TempId() {
        return h5TempId;
    }

    public void setH5TempId(String h5TempId) {
        this.h5TempId = h5TempId;
    }


    public Integer getDmDensity() {
        return dmDensity;
    }

    public void setDmDensity(Integer dmDensity) {
        this.dmDensity = dmDensity;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }
}
