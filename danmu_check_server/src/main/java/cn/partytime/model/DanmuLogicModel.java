package cn.partytime.model;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/8/19.
 */

public class DanmuLogicModel implements Serializable {

    private static final long serialVersionUID = 2436270031898257560L;

    /**弹幕编号*/
    private String id;

    /**弹幕池编号*/
    private String poolId;

    /**消息类型*/
    private String type;

    /**活动地址编号*/
    private String addressId;

    /**
     * 活动编号
     */
    private String partyId;

    /**
     * 是否屏蔽
     */
    private boolean blocked;

    /**弹幕来源类型 0：测试弹幕；1：测试弹幕；2：屏幕弹幕（管理员发送）*/
    private String danmuType;
    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private String status;

    /**
     * 消息体
     */
    private String msg;

    /**
     * 颜色
     */
    private String color;

    /**
     * 特效
     */
    private String expression;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getDanmuType() {
        return danmuType;
    }

    public void setDanmuType(String danmuType) {
        this.danmuType = danmuType;
    }


    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
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
}
