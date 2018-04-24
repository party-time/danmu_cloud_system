package cn.partytime.model;

import cn.partytime.model.manager.ParamTemplate;

/**
 * Created by liuwei on 2016/9/8.
 */
public class DanmuClientListResult {

    private String id;

    private String name;

    private String registCode;

    private String overdueStr;

    private Integer isHaveClient;

    private String paramTemplateId;

    private Integer screenId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistCode() {
        return registCode;
    }

    public void setRegistCode(String registCode) {
        this.registCode = registCode;
    }

    public String getOverdueStr() {
        return overdueStr;
    }

    public void setOverdueStr(String overdueStr) {
        this.overdueStr = overdueStr;
    }

    public Integer getIsHaveClient() {
        return isHaveClient;
    }

    public void setIsHaveClient(Integer isHaveClient) {
        this.isHaveClient = isHaveClient;
    }

    public String getParamTemplateId() {
        return paramTemplateId;
    }

    public void setParamTemplateId(String paramTemplateId) {
        this.paramTemplateId = paramTemplateId;
    }

    public Integer getScreenId() {
        return screenId;
    }

    public void setScreenId(Integer screenId) {
        this.screenId = screenId;
    }
}
