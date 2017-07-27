package cn.partytime.model.client;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by liuwei on 16/6/12.
 * 客户端主
 */
@Document(collection = "danmu_client_master")
public class DanmuClientMaster extends BaseModel{


    private String id;

    /**
     * 弹幕主客户端的唯一码
     */
    private String danmuClientMasterCode;

    /**
     * 弹幕主客户端关联的管理员
     */
    private String adminUserId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDanmuClientMasterCode() {
        return danmuClientMasterCode;
    }

    public void setDanmuClientMasterCode(String danmuClientMasterCode) {
        this.danmuClientMasterCode = danmuClientMasterCode;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

}
