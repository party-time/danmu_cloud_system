package cn.partytime.oldModel;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by liuwei on 16/6/15.
 * 预制弹幕
 */

@Document(collection = "pre_danmu_old")
public class PreDanmuModel_Old extends BaseModel {

    @Field("_id")
    private String id;

    //颜色
    private String color;

    //弹幕内容
    private String msg;

    //弹幕库的id
    private String danmuLibraryId;

    //预制弹幕出现的时间
    private Date preTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getPreTime() {
        return preTime;
    }

    public void setPreTime(Date preTime) {
        this.preTime = preTime;
    }

    public String getDanmuLibraryId() {
        return danmuLibraryId;
    }

    public void setDanmuLibraryId(String danmuLibraryId) {
        this.danmuLibraryId = danmuLibraryId;
    }

}
