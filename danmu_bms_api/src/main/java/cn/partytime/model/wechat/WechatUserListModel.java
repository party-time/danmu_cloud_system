package cn.partytime.model.wechat;

import cn.partytime.model.manager.DanmuAddress;

/**
 * Created by administrator on 2017/4/14.
 */
public class WechatUserListModel {

    private WechatUser wechatUser;

    private WechatUserInfo wechatUserInfo;

    private DanmuAddress registAddress;

    private DanmuAddress lastAddress;

    public WechatUser getWechatUser() {
        return wechatUser;
    }

    public void setWechatUser(WechatUser wechatUser) {
        this.wechatUser = wechatUser;
    }

    public WechatUserInfo getWechatUserInfo() {
        return wechatUserInfo;
    }

    public void setWechatUserInfo(WechatUserInfo wechatUserInfo) {
        this.wechatUserInfo = wechatUserInfo;
    }

    public DanmuAddress getRegistAddress() {
        return registAddress;
    }

    public void setRegistAddress(DanmuAddress registAddress) {
        this.registAddress = registAddress;
    }

    public DanmuAddress getLastAddress() {
        return lastAddress;
    }

    public void setLastAddress(DanmuAddress lastAddress) {
        this.lastAddress = lastAddress;
    }
}
