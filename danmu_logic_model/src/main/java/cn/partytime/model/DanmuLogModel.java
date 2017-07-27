package cn.partytime.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;


@Data
public class DanmuLogModel {
    private String id;

    /**弹幕编号*/
    private String danmuId;

    /**
     * 模板编号
     */
    private String templateId;

    /**
     * 弹幕内容
     */
    private Map<String,Object> content;



    //弹幕池
    private String danmuPoolId;

    /**审核员编号*/
    private String checkUserId;

    /***弹幕来源 管理员:0,微信用户:1*/
    private int danmuSrc;

    /**0:非语音 1：语音弹幕*/
    private int type;

    /**
     * 开始时刻（当前时间-电影开始时间）
     */
    private int time;
    /**屏蔽状态*/
    private Boolean isBlocked = false;
    /**是否查看状态*/
    private boolean viewFlg=false;
    private Integer isDelete = 0;

    private String createUserId;

    private Date createTime = new Date();

    private String updateUserId;

    private Date updateTime = new Date();
}
