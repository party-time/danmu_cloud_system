package cn.partytime.model;

import lombok.Data;

import java.util.Date;


@Data
public class WechatUserWeekCountDto {

    private String id;

    private String addressId;

    private Date startDate;

    private Date endDate;

    private int count;

    private Integer isDelete = 0;

    private String createUserId;

    private Date createTime = new Date();

    private String updateUserId;

    private Date updateTime = new Date();
}
