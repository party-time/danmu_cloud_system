package cn.partytime.model;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/11/22.
 */
public class ServerInfo implements Serializable {


    private static final long serialVersionUID = 7022124946049297729L;

    /**
     * 端口号
     */
    private int port;

    /**
     * ip
     */
    private String ip;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
