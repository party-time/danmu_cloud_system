package cn.partytime.model;

import java.io.Serializable;

/**
 * Created by task on 2016/6/21.
 */


public class DanmuCollectorInfo implements Serializable {


    private static final long serialVersionUID = 5733644534734550530L;
    /**
     * 弹幕服务器地址
     */
    private String ip;

    /**
     * 弹幕服务器端口
     */
    private int port;

    /**
     * 连接数
     */
    private int count;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
