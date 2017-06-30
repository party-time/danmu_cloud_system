package cn.partytime.model.manager.danmuCmdJson;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by administrator on 2017/5/8.
 */
public class CmdComponentValue extends BaseModel {

    @Field("_id")
    private String id;

    //组件的id
    private String componentId;

    //组件值的名称
    private String name;

    //组件值的值
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
