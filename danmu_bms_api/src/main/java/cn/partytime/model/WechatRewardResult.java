package cn.partytime.model;

import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.H5Template;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.WechatReward;
import cn.partytime.model.wechat.WechatUser;

/**
 * Created by administrator on 2016/11/29.
 */
public class WechatRewardResult {

    private Party party;

    private DanmuAddress danmuAddress;

    private WechatReward wechatReward;

    private WechatUser wechatUser;

    private H5Template h5Template;

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public DanmuAddress getDanmuAddress() {
        return danmuAddress;
    }

    public void setDanmuAddress(DanmuAddress danmuAddress) {
        this.danmuAddress = danmuAddress;
    }

    public WechatReward getWechatReward() {
        return wechatReward;
    }

    public void setWechatReward(WechatReward wechatReward) {
        this.wechatReward = wechatReward;
    }

    public WechatUser getWechatUser() {
        return wechatUser;
    }

    public void setWechatUser(WechatUser wechatUser) {
        this.wechatUser = wechatUser;
    }

    public H5Template getH5Template() {
        return h5Template;
    }

    public void setH5Template(H5Template h5Template) {
        this.h5Template = h5Template;
    }
}
