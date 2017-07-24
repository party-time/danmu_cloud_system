package cn.partytime.model.manager;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by liuwei on 16/6/12.
 * 弹幕活动的地址
 */
@Document(collection = "danmu_check_config")
public class DanmuCheckConfig extends BaseModel {

    private String id;

    private String pooId;

    /**
     * 严格度
     */
    private int strictstatus;

    /**
     * 延迟时间
     */
    private int delayTime;

    /**
     * 处理时长
     */
    private int dealTime;

    /**
     * 处理弹幕数量
     */
    private int danmuCount;
    /**
     * 管理员编号
     */
    private String adminId;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPooId() {
        return pooId;
    }

    public void setPooId(String pooId) {
        this.pooId = pooId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public int getStrictstatus() {
        return strictstatus;
    }

    public void setStrictstatus(int strictstatus) {
        this.strictstatus = strictstatus;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public int getDealTime() {
        return dealTime;
    }

    public void setDealTime(int dealTime) {
        this.dealTime = dealTime;
    }

    public int getDanmuCount() {
        return danmuCount;
    }

    public void setDanmuCount(int danmuCount) {
        this.danmuCount = danmuCount;
    }
}
