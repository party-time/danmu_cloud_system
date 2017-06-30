package cn.partytime.oldModel;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by lENOVO on 2016/10/25.
 */

@Document(collection = "ad_timer_danmu_old")
public class AdTimerDanmu_Old extends BaseModel {

    @Field("_id")
    private String id;

    /**广告弹幕库编号*/
    private String libraryId;

    /***弹幕类型 0:弹幕；1:动漫；2：图片；3：闪光字；4：表情*/
    private Integer type;

    private String typeName;

    /**特效id*/
    private String code;
    /**颜色*/
    private String color;

    /**内容*/
    private String content;

    /**时间*/
    private Integer beginTime;

    /**位置*/
    private Integer direction;
    /**
     * 结束时间
     */
    private Integer endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Integer beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
}
