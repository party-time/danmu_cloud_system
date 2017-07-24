package cn.partytime.model;

import cn.partytime.model.manager.ResourceFile;

import java.util.Date;
import java.util.List;

/**
 * Created by liuwei on 2016/9/27.
 */
public class PartyResourceJson {

    private String id;

    private String name;

    private Date startDate;

    private Date endDate;

    private List<ResourceJson> expressions;

    private List<ResourceJson> specialEffects;

    private List<ResourceFile> localVideoUrl;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<ResourceJson> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<ResourceJson> expressions) {
        this.expressions = expressions;
    }

    public List<ResourceJson> getSpecialEffects() {
        return specialEffects;
    }

    public void setSpecialEffects(List<ResourceJson> specialEffects) {
        this.specialEffects = specialEffects;
    }

    public List<ResourceFile> getLocalVideoUrl() {
        return localVideoUrl;
    }

    public void setLocalVideoUrl(List<ResourceFile> localVideoUrl) {
        this.localVideoUrl = localVideoUrl;
    }
}
