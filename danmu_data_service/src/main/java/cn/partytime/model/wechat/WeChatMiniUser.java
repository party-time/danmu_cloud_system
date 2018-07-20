package cn.partytime.model.wechat;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Administrator on 2018/7/20 0020.
 */
@Document(collection = "weChatMiniUser")
public class WeChatMiniUser extends BaseModel {

    private String id;

    private String unionId;

    private String openId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
