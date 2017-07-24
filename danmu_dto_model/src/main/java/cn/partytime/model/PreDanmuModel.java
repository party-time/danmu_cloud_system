package cn.partytime.model;

import lombok.Data;

import java.util.Map;

@Data
public class PreDanmuModel {

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

}