package cn.partytime.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by administrator on 2016/11/30.
 */

@Data
public class MovieSchedule {

    private String id;

    /**
     * 活动id
     */
    private String partyId;

    /**
     * 场地id
     */
    private String addressId;

    /**
     *排片的开始时间
     */
    private Date startTime;

    /**
     * 电影开始时间
     */
    private Date moviceStartTime;
    /**
     *排片的结束时间
     */
    private Date endTime;

    /**
     * 场次
     */
    private Integer number;

    /**
     * 客户端时间
     */
    private Long clientStartTime;

    private Long clientMoviceStartTime;

    private Long clientEndTime;

    private Integer isDelete = 0;

    private String createUserId;

    private Date createTime = new Date();

    private String updateUserId;

    private Date updateTime = new Date();


}
