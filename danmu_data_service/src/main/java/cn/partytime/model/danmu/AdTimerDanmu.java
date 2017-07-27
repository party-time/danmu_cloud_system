package cn.partytime.model.danmu;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

/**
 * Created by lENOVO on 2016/10/25.
 */

@Document(collection = "ad_timer_danmu")
public class AdTimerDanmu extends BaseModel {


    private String id;

    /**
     * 广告弹幕库编号
     */
    private String libraryId;



    /**
     * 时间
     */
    private Integer beginTime;
    /**
     * 结束时间
     */
    private Integer endTime;


    /**
     * 弹幕内容
     */
    private Map<String, Object> content;

    /**
     * 模板编号
     */
    private String templateId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
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

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}

