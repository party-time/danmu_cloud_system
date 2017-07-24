package cn.partytime.model.wechat;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by administrator on 2016/11/30.
 */
@Document(collection = "wechat_user_log")
public class WechatUserLog extends BaseModel{

    private String id;

    private String openId;

    private String log;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
