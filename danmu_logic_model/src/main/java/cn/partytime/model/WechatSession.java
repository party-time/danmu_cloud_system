package cn.partytime.model;

import lombok.Data;

/**
 * Created by administrator on 2017/7/25.
 */

@Data
public class WechatSession {

    private String openId;

    private PartyLogicModel partyLogicModel;

    //private DanmuAddress danmuAddress;

}
