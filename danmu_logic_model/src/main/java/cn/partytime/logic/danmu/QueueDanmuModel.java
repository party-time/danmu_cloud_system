package cn.partytime.logic.danmu;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by task on 16/6/28.
 */
public class QueueDanmuModel implements Serializable {

    private static final long serialVersionUID = -6364322331039421689L;
    /**
     * 弹幕id
     */
    private String id;

    /**
     * 弹幕类型
     */
    private String type;

    /**
     * 弹幕池编号
     */
    private String poolId;

    /**地址编号*/
    private String addressId;

    /**
     * 活动编号
     */
    private String partyId;

    /**
     * 弹幕内容
     */
    private String msg;


    /**
     * 弹幕颜色
     */
    private String color;

    /**
     * 用户编号
     */
    private String userId;


    /**
     * 弹幕图片
     */
    private String expresssion;


    /**弹幕来源类型 1：手机，0：管理员*/
    private int danmuSrcType;

    /**微信公众号*/
    private String openId;

    /**昵称*/
    private String nick;

    /**用户图片*/
    private String imgUrl;

    /**性别*/
    private Integer sex;

    /**
     * 弹幕创建时间
     */
    private Date createTime;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getExpresssion() {
        return expresssion;
    }

    public void setExpresssion(String expresssion) {
        this.expresssion = expresssion;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public int getDanmuSrcType() {
        return danmuSrcType;
    }

    public void setDanmuSrcType(int danmuSrcType) {
        this.danmuSrcType = danmuSrcType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
