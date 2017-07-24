package cn.partytime.model.manager;

import cn.partytime.baseModel.BaseModel;

/**
 * Created by administrator on 2017/3/21.
 *
 * 设备的ip信息
 */
public class DeviceIpInfo extends BaseModel {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
