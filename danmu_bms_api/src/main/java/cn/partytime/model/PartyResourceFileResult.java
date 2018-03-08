package cn.partytime.model;

import cn.partytime.model.manager.ResourceFile;

import java.util.List;

/**
 * Created by administrator on 2016/11/21.
 */
public class PartyResourceFileResult {

    private ResourceFile h5BackgroundFile;

    private List<ResourceFile> expressions;

    private List<ResourceFile> specialImages;

    private List<ResourceFile> specialVideos;

    private List<ResourceFile> expressionsConstant;

    public ResourceFile getH5BackgroundFile() {
        return h5BackgroundFile;
    }

    public void setH5BackgroundFile(ResourceFile h5BackgroundFile) {
        this.h5BackgroundFile = h5BackgroundFile;
    }


    public List<ResourceFile> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<ResourceFile> expressions) {
        this.expressions = expressions;
    }

    public List<ResourceFile> getSpecialImages() {
        return specialImages;
    }

    public void setSpecialImages(List<ResourceFile> specialImages) {
        this.specialImages = specialImages;
    }

    public List<ResourceFile> getSpecialVideos() {
        return specialVideos;
    }

    public void setSpecialVideos(List<ResourceFile> specialVideos) {
        this.specialVideos = specialVideos;
    }

    public List<ResourceFile> getExpressionsConstant() {
        return expressionsConstant;
    }

    public void setExpressionsConstant(List<ResourceFile> expressionsConstant) {
        this.expressionsConstant = expressionsConstant;
    }
}
