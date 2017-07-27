package cn.partytime.model.manager.danmuCmdJson;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by administrator on 2017/5/8.
 */
@Document(collection = "cmd_temp")
public class CmdTemp extends BaseModel {


    private String id;

    //中文名称
    private String name;

    //用于在json体内做逻辑使用
    private String key;

    //是否入弹幕库 0入库  1不入库
    private Integer isInDanmuLib;

    //是否发送到H5界面 0 发送 1不发送
    private Integer isSendH5;

    //排序字段
    private Integer sort;

    //是否展示 0是  1否s
    private Integer show;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getIsInDanmuLib() {
        return isInDanmuLib;
    }

    public void setIsInDanmuLib(Integer isInDanmuLib) {
        this.isInDanmuLib = isInDanmuLib;
    }

    public Integer getIsSendH5() {
        return isSendH5;
    }

    public void setIsSendH5(Integer isSendH5) {
        this.isSendH5 = isSendH5;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getShow() {
        return show;
    }

    public void setShow(Integer show) {
        this.show = show;
    }
}
