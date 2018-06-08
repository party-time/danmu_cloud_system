package cn.partytime.model.danmu;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by admin on 2018/6/7.
 */
@Document(collection = "payDanmu")
public class PayDanmu extends BaseModel {

    private String id;

    private String danmuId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDanmuId() {
        return danmuId;
    }

    public void setDanmuId(String danmuId) {
        this.danmuId = danmuId;
    }
}
