package cn.partytime.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by liuwei on 16/7/21.
 */

@Data
public class WechatUser {

    private String id;

    /**
     * 用户编号
     */
    private String userId;

    private String nick;

    private String imgUrl;

    private Integer sex;

    private String city;

    private String country;

    private String province;

    private String language;

    private String unionId;

    private String openId;

    private Long subscribeTime;

    private Integer subscribeState;

    private Date lastOpenDate;

    private Date createDate;

    private Double latitude;

    private Double longitude;

    private Date getLocationTime;

    //管理员分配场地的时间，15分钟失效
    private Date assignAddressTime;

}
