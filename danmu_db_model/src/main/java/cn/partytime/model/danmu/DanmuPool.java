package cn.partytime.model.danmu;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 * 弹幕池
 */
@Document(collection = "danmu_pool")
public class DanmuPool extends BaseModel {

    private String id;

    /**
     * 一组弹幕池，用于完成弹幕池合并
     */
    private List<String> danmuPoolIdList;

    /**
     * 活动与地址关联关系的id
     */
    private String partyAddressRelationId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartyAddressRelationId() {
        return partyAddressRelationId;
    }

    public void setPartyAddressRelationId(String partyAddressRelationId) {
        this.partyAddressRelationId = partyAddressRelationId;
    }

    public List<String> getDanmuPoolIdList() {
        return danmuPoolIdList;
    }

    public void setDanmuPoolIdList(List<String> danmuPoolIdList) {
        this.danmuPoolIdList = danmuPoolIdList;
    }
}
