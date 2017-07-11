package cn.partytime.model.danmu;

import java.util.Map;

/**
 * Created by dm on 2017/5/25.
 */
public class PreDanmuViewModel {


    private String id;

    /**
     * 弹幕内容
     */
    private Object msg;

    /**
     * 弹幕内容
     */
    private Map<String,Object> content;

    /**
     * 模板编号
     */
    private String templateId;


    /**弹幕类型*/
    private Integer danmuType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
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

    public Integer getDanmuType() {
        return danmuType;
    }

    public void setDanmuType(Integer danmuType) {
        this.danmuType = danmuType;
    }
}
