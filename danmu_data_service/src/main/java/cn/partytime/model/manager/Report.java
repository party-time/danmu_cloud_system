package cn.partytime.model.manager;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by administrator on 2017/8/29.
 */
@Document(collection = "danmu_report")
public class Report {

    private String id;

    //举报的弹幕的id
    private String danmuId;

    private Date createTime=new Date();

    //举报人的id
    private String wechatId;

    //处理0   不处理1
    private Integer status;


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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
