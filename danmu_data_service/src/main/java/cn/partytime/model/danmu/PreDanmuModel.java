package cn.partytime.model.danmu;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Map;

/**
 * Created by liuwei on 16/6/15.
 * 预制弹幕
 */

@Document(collection = "pre_danmu")
public class PreDanmuModel extends BaseModel {

    @Field("_id")
    private String id;

    /**
     * 弹幕内容
     */
    private Map<String,Object> content;

    /**
     * 模板编号
     */
    private String templateId;

    //弹幕库的id
    private String danmuLibraryId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDanmuLibraryId() {
        return danmuLibraryId;
    }

    public void setDanmuLibraryId(String danmuLibraryId) {
        this.danmuLibraryId = danmuLibraryId;
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
