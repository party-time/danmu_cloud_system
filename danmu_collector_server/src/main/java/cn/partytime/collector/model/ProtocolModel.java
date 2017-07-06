package cn.partytime.collector.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/8/31.
 */

@Data
public class ProtocolModel<T> implements Serializable {


    private static final long serialVersionUID = 793345282313615131L;

    /**
     * 类型
     */
    private String type;

    /**
     * 唯一标识
     */
    private String code;

    /**
     * 消息来源:管理员：0；用户：1
     */
    private Integer messageSrc;

    /**
     * 客户端类型 设备：0；手机端：1  javaClient:2
     */
    private String clientType;

    /**
     * 屏幕编号
     */
    private Integer screenId;


    /**
     * 当前正在进行的活动
     */
    private String partyId;


    T data;


}
