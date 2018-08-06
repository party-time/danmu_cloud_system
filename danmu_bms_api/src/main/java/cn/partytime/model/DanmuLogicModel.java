package cn.partytime.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by lENOVO on 2016/10/17.
 */

@Data
public class DanmuLogicModel {


    private String id;

    private String color;

    private Object msg;

    private Boolean isBlocked = false;
    //弹幕池
    private String danmuPoolId;

    /***弹幕来源 管理员:0,微信用户:1*/
    private int danmuSrc;

    /**
     * 弹幕key
     */
    private String key;

    /**弹幕类型 0:普通弹幕.1:语音弹幕*/
    private int type;

    private int time;

    private String url;

    private String openId;

    private String createUserId;

    private String nick;

    private Date createTime = new Date();

    private boolean isSend;


    /**
     * 发送状态 0：未发送 1：已发送  2：发送成功
     */
    private int sendStatus = 0;

    /**
     * 弹幕类型
     */
    private String danmuTypeName;


    /**
     * 管理员接收用户发送弹幕的时间
     */
    private Date adminAccepetTime;


    /**
     * 管理员审核后推送给用户的时间
     */
    private Date sendUserTime;
}
