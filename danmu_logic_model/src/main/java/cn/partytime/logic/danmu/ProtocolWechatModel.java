package cn.partytime.logic.danmu;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/11/24.
 */
public class ProtocolWechatModel implements Serializable {


    private static final long serialVersionUID = 2559706319171891239L;

    /**类型*/
    private String type;

    /**名称*/
    private String name;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
