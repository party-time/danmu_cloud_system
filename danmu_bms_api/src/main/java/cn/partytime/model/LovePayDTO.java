package cn.partytime.model;

import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.LovePay;
import cn.partytime.model.manager.Party;
import cn.partytime.model.wechat.WechatUser;

/**
 * Created by administrator on 2017/7/25.
 */
public class LovePayDTO {

    private LovePay lovePay;

    private WechatUser wechatUser;

    private Party party;

    private DanmuAddress danmuAddress;


    public LovePay getLovePay() {
        return lovePay;
    }

    public void setLovePay(LovePay lovePay) {
        this.lovePay = lovePay;
    }

    public WechatUser getWechatUser() {
        return wechatUser;
    }

    public void setWechatUser(WechatUser wechatUser) {
        this.wechatUser = wechatUser;
    }

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
}
