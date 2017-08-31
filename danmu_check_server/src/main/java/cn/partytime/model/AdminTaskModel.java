package cn.partytime.model;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * Created by lENOVO on 2016/10/11.
 */

@Data
public class AdminTaskModel {
    /*管理员编号*/
    private String adminId;

    private String longChannelId;

    private String shortChannelId;

    private String adminName;

    private Integer partyType;

    /**
     *  秘钥
     */
    private String authKey;

    /**活动编号*/
    private String partyId;

    /**地址编号*/
    private String addressId;

    /**管理员类型*/
    private String type;

    /*任务数*/
    private int count;

    private Channel channel;

    private int checkFlg;
}
