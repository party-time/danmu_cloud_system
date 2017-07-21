package cn.partytime.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/28.
 */


@Data
public class PartyLogicModel implements Serializable{

    private String partyId;

    private String partyName;

    private String addressId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 电影开始时间
     */
    private Date activeTime;

    /**
     * 活动状态
     */
    private int status;

    /**
     * 获取类型 0:活动场：1：弹幕场
     */
    private int type;


    /**
     * 场地类型 0 固定场地  1 临时场地
     */
    private int addressType;



    private String shortName;

    /**
     * H5模版的id
     */
    private String h5TempId;

    /**
     * 弹幕密度
     */
    private Integer dmDensity;

    private long movieTime;

}
