package cn.partytime.model;

import lombok.Data;

import java.util.Map;

@Data
public class TimerDanmuModel {

    private String id;

    /**活动编号*/
    private String partyId;


    /**时间*/
    private Integer beginTime;
    /**
     * 结束时间
     */
    private Integer endTime;

    /**
     * 弹幕内容
     */
    private Map<String,Object> content;

    /**
     * 模板编号
     */
    private String templateId;
}
