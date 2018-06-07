package cn.partytime.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/9/1.
 */

@Data
public class DanmuClientInfoModel implements Serializable {
    private static final long serialVersionUID = 7757607862683701760L;

    private String id;
    /*客户端名称*/
    private String name;
    /**
     * 注册码
     */
    private String registCode;

    /**
     * 客户端类型 设备：0；手机端：1
     */
    private int clientType;
    /**
     * 地址编号
     */
    private String addressId;

    /**
     * 活动编号
     */
    private String partyId;


    /**屏幕编号*/
    private int screenId;

    /**
     * 由客户端生成的唯一标识
     */
    private String danmuClientCode;

    /**
     *屏幕弹幕数
     */
    private Integer danmuCount;

    private Long lastTime;

    private String ip;

    private String port;

}
