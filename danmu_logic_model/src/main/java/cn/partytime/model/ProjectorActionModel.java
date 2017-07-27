package cn.partytime.model;


import lombok.Data;

import java.util.Date;

@Data
public class ProjectorActionModel {

    private String id;

    /**
     * 注册码
     */
    private String registerCode;


    private Date startTime;


    private Date  endTime;

    private Date createTime;

    private Date updateTime;
}
