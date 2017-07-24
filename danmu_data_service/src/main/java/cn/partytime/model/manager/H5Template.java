package cn.partytime.model.manager;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by administrator on 2017/4/28.
 */
@Document(collection = "h5_template")
public class H5Template extends BaseModel{

    @Field("_id")
    private String id;

    /**
     * 模版标题
     */
    private String tempTitle;

    /**
     * 访问h5的url
     */
    private String h5Url;

    /**
     * h5的html内容
     */
    private String html;

    /**
     * 是否是首页 0首页  1非首页
     */
    private Integer isIndex;

    /**
     * ftl还是html 0ftl   1html
     */
    private Integer type;

    /**
     * 是否是基础页，基础页面只能有唯一的一个，所有party的首页
     */
    private Integer isBase = 1;

    /**
     * 支付金额
     */
    private Integer payMoney;

    /**
     * 支付时收到的文案
     */
    private String payTitle;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTempTitle() {
        return tempTitle;
    }

    public void setTempTitle(String tempTitle) {
        this.tempTitle = tempTitle;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Integer getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(Integer isIndex) {
        this.isIndex = isIndex;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsBase() {
        return isBase;
    }

    public void setIsBase(Integer isBase) {
        this.isBase = isBase;
    }

    public Integer getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Integer payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayTitle() {
        return payTitle;
    }

    public void setPayTitle(String payTitle) {
        this.payTitle = payTitle;
    }
}
