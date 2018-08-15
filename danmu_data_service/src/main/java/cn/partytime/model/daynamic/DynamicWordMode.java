package cn.partytime.model.daynamic;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "wechat_dynamic")
public class DynamicWordMode extends BaseModel {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
