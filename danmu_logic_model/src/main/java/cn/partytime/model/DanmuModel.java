package cn.partytime.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;


@Data
public class DanmuModel {
    private String id;
    /**
     * 弹幕内容
     */
    private Map<String,Object> content;


    private Boolean isBlocked = false;
    //弹幕池
    private String danmuPoolId;

    private String checkUserId;

    /***弹幕来源 管理员:0,微信用户:1*/
    private int danmuSrc;

    /**0:非语音 1：语音弹幕*/
    private int type;


    /**
     * 模板编号
     */
    private String templateId;


    /**
     * 开始时刻（当前时间-电影开始时间）
     */
    private int time;

    /**是否查看状态*/
    private boolean viewFlg=false;

    private Integer isDelete = 0;

    private String createUserId;

    private Date createTime = new Date();

    private String updateUserId;

    private Date updateTime = new Date();


    /**
     * 模板Key
     */
    private String templateIdKey;


    /**
     * 发送状态 0：未发送 1：已发送  2：发送成功
     */
    private int sendStatus = 0;
}

