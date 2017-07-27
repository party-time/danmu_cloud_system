package cn.partytime.model.shop;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by administrator on 2017/7/7.
 */
@Document(collection = "order")
public class Order {


    private String id;

    private String partyId;
    private String addressId;
    private String openId;
    private String timestamp;
    private String detail;
    private Integer total_fee;
    private String clientIp;

    /**
     * 商品ID
     */
    private String itemId;

    /**
     * 购买的数量
     */
    private Integer num;
    /**
     * 支付状态 0 支付成功 1未支付 2支付失败
     */
    private Integer status;

    /**
     * 购物的时间
     */
    private Date shopTime = new Date();

    /**
     * 获得商品的时间
     */
    private Date getItemTime;

    /**
     * 将商品给用户的管理员id
     */
    private String adminId;

    /**
     * 购买时间
     */
    private Date createTime=new Date();

    /**
     * 生成的验证码
     */
    private String code;

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

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getShopTime() {
        return shopTime;
    }

    public void setShopTime(Date shopTime) {
        this.shopTime = shopTime;
    }

    public Date getGetItemTime() {
        return getItemTime;
    }

    public void setGetItemTime(Date getItemTime) {
        this.getItemTime = getItemTime;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
