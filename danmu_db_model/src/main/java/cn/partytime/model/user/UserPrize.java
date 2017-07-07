package cn.partytime.model.user;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by lENOVO on 2016/10/18.
 */

@Document(collection = "userPrize")
public class UserPrize extends BaseModel {

    private String id;

    private String openId;

    /**弹幕编号*/
    private String danmuId;

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

    public String getDanmuId() {
        return danmuId;
    }

    public void setDanmuId(String danmuId) {
        this.danmuId = danmuId;
    }
}
