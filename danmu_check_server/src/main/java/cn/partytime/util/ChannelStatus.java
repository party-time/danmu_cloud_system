package cn.partytime.util;

import java.io.Serializable;

/**
 * Created by user on 16/6/24.
 */
public class ChannelStatus implements Serializable {

    private static final long serialVersionUID = 7643475710466742923L;

    /**
     * 弹幕池
     */
    private String poolId;
    /**
     * 用户编号
     */
    private String userId;

    /**
     * 登陆状态
     */
    private int status;

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
