package cn.partytime.model;

import lombok.Data;

@Data
public class DeviceIpInfoModel {

    private String id;

    /**
     * 场地id
     */
    private String addressId;

    /**
     * ip信息
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 类型 0 投影ip  1 javaClient ip
     */
    private Integer type;

    /**
     * 编号
     */
    private Integer number;

    /**
     * url
     */
    private String url;

}
