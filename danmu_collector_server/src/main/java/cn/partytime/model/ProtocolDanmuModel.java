package cn.partytime.model;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/11/24.
 */
public class ProtocolDanmuModel implements Serializable{


    private static final long serialVersionUID = -2762832861681483053L;

    /**
     * 弹幕编号
     */
    private String id;

    /**弹幕类型*/
    private String type;

    /**
     * 弹幕名称
     */
    private String name;

    /**
     * 弹幕颜色
     */
    private String color;

    /**
     * 名称后缀
     */
    private String suffix;

    /**
     * 弹幕内容
     */
    private String message;


    private Integer status;

}
