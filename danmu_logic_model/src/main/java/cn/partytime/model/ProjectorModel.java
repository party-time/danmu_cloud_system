package cn.partytime.model;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectorModel {

    private String id;

    /**
     * 注册码
     */
    private String registerCode;


    private Date startTime;


    private long usedTime;


    private int realUsedHours;


    private Date updateTime;


}
