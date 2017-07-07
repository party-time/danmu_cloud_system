package cn.partytime.model.wechat;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by administrator on 2017/4/13.
 */
@Document(collection = "wechat_user_info")
public class WechatUserInfo {

    private String id;

    /**
     * 微信id
     */
    private String wechatId;

    /**
     * 首次关注时的地理位置
     */
    private Double registLongitude;

    private Double registLatitude;

    /**
     * 首次关注的时间
     */
    private Date registDate;

    /**
     * 最近一次打开公众账号的地理位置信息
     */
    private Double lastLongitude;

    private Double lastLatitude;

    /**
     * 最近一次打开公众账号的时间
     */
    private Date lastGetLocationDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public Double getRegistLongitude() {
        return registLongitude;
    }

    public void setRegistLongitude(Double registLongitude) {
        this.registLongitude = registLongitude;
    }

    public Double getRegistLatitude() {
        return registLatitude;
    }

    public void setRegistLatitude(Double registLatitude) {
        this.registLatitude = registLatitude;
    }

    public Date getRegistDate() {
        return registDate;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    public Double getLastLongitude() {
        return lastLongitude;
    }

    public void setLastLongitude(Double lastLongitude) {
        this.lastLongitude = lastLongitude;
    }

    public Double getLastLatitude() {
        return lastLatitude;
    }

    public void setLastLatitude(Double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }

    public Date getLastGetLocationDate() {
        return lastGetLocationDate;
    }

    public void setLastGetLocationDate(Date lastGetLocationDate) {
        this.lastGetLocationDate = lastGetLocationDate;
    }
}
