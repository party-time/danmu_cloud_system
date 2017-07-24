package cn.partytime.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by task on 2016/6/21.
 */

@Data
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
    private Integer count;

}
