package cn.partytime.model.manager;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by administrator on 2017/2/23.
 */
public class Param extends BaseModel {

    private String id;

    //参数的名称
    private String name;

    //0数字 1布尔值 2字符串 3数组 4其他paramTemplate
    private Integer valueType;

    //5其他paramTemplate的ID
    private String paramTemplateId;

    //参数的描述
    private String des;

    //参数的默认值
    private String defaultValue;

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

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    public String getParamTemplateId() {
        return paramTemplateId;
    }

    public void setParamTemplateId(String paramTemplateId) {
        this.paramTemplateId = paramTemplateId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
