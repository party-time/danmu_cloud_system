package cn.partytime.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by liuwei on 16/6/12.
 * 弹幕活动
 */

@Data
public class PartyModel {

    private String id;

    private String name;

    /**
     * 0活动   1电影
     */
    private Integer type;

    private Date startTime;

    /**
     * 内容开始时间
     */
    private Date activityStartTime;

    /**
     * 活动状态 0：未开始，1：活动开始，2：电影开始，3：电影结束 4：电影下线
     */
    private int status;

    /**
     * 内容结束时间
     */
    //private Date activityEndTime;

    private Date endTime;

    private String startTimeStr;

    private String endTimeStr;

    /**
     * 拼音缩写
     */
    private String shortName;

    /**
     * 最后一次更新资源的时间
     */
    private Date lastUpdateResourceTime;

    /**
     * 电影代号
     */
    private String movieAlias;

    /**
     * 弹幕开始时间
     */
    private long danmuStartTime;

    /**
     * 关联的页面模版id
     */
    private String h5TempId;

    /**
     * 弹幕密度
     */
    private Integer dmDensity;


    /**
     * 电影总时长
     */
    private long movieTime;

    private Integer isDelete = 0;

    private String createUserId;

    private Date createTime = new Date();

    private String updateUserId;

    private Date updateTime = new Date();


}

