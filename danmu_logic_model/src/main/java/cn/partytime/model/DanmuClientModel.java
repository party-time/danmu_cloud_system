package cn.partytime.model;


import lombok.Data;

@Data
public class DanmuClientModel {
    private String id;

    /*客户端名称*/
    private String name;
    /**
     * 注册码
     */
    private String registCode;

    /**
     * 地址编号
     */
    private String addressId;

    /**屏幕编号*/
    private int screenId;

    /**
     * 由客户端生成的唯一标识
     */
    private String danmuClientCode;

    /**
     * 客户端关联的参数模版
     */
    private String paramTemplateId;

}
