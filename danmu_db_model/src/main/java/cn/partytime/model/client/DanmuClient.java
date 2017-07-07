package cn.partytime.model.client;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by liuwei on 16/6/12.
 * 弹幕客户端
 */
@Document(collection = "danmu_client")
public class DanmuClient extends BaseModel{

    private String id;

    /*客户端名称*/
    private String name;
    /**
     * 注册码
     */
    private String registCode;

    /**
     * 地址编号
     */
    private String addressId;

    /**屏幕编号*/
    private int screenId;

    /**
     * 由客户端生成的唯一标识
     */
    private String danmuClientCode;

    /**
     * 客户端关联的参数模版
     */
    private String paramTemplateId;


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

    public String getDanmuClientCode() {
        return danmuClientCode;
    }

    public void setDanmuClientCode(String danmuClientCode) {
        this.danmuClientCode = danmuClientCode;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public String getParamTemplateId() {
        return paramTemplateId;
    }

    public void setParamTemplateId(String paramTemplateId) {
        this.paramTemplateId = paramTemplateId;
    }
}
