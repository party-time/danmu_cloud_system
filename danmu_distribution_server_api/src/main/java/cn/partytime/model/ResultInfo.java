package cn.partytime.model;


import java.util.Map;
import java.io.Serializable;

/**
 * Created by task on 2016/6/21.
 */


public class ResultInfo implements Serializable {

    private static final long serialVersionUID = -1436782464996999057L;
    /**
     * 状态码
     */
    private int code;

    /**
     * 消息
     */
    private String message;

    /**
     * 服务信息
     */
    private ServerInfo serverInfo;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }
}
