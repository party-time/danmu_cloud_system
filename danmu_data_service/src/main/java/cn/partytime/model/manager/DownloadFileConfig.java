package cn.partytime.model.manager;

import java.util.List;

/**
 * Created by liuwei on 2016/8/19.
 */
public class DownloadFileConfig {
    /**
     * 表情图片列表
     */
    private List<String> expressions;

    /**
     * 特效图片列表
     */
    private List<String> specialImages;

    /**
     * 特效视频列表
     */
    private List<String> specialVideos;

    public List<String> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<String> expressions) {
        this.expressions = expressions;
    }

    public List<String> getSpecialImages() {
        return specialImages;
    }

    public void setSpecialImages(List<String> specialImages) {
        this.specialImages = specialImages;
    }

    public List<String> getSpecialVideos() {
        return specialVideos;
    }

    public void setSpecialVideos(List<String> specialVideos) {
        this.specialVideos = specialVideos;
    }
}
