package cn.partytime.model.client;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by liuwei on 16/6/12.
 * 弹幕客户端组
 */
@Document(collection = "danmu_client_group")
public class DanmuClientGroup extends BaseModel{

    private String id;

    private List<String> danmuClientIdList;

    private String creatorId;


    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getDanmuClientIdList() {
        return danmuClientIdList;
    }

    public void setDanmuClientIdList(List<String> danmuClientIdList) {
        this.danmuClientIdList = danmuClientIdList;
    }
}
