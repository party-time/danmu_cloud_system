package cn.partytime.model.manager;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by administrator on 2017/2/23.
 */
public class ParamValue extends BaseModel {

    private String id;

    //关联的对象的主键
    private String objId;

    /**
     * 关联对象的类型，避免id冲突
     * 0 flash客户端
     */
    private Integer type;

    //对应模版的参数的id
    private String paramId;

    //对应的值
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
