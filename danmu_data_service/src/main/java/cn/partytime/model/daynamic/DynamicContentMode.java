package cn.partytime.model.daynamic;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "wechat_dynamic_content")
public class DynamicContentMode extends BaseModel {

    private String id;

    private String dynamicWord;

    private String content;

    private String voicePath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDynamicWord() {
        return dynamicWord;
    }

    public void setDynamicWord(String dynamicWord) {
        this.dynamicWord = dynamicWord;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }
}
