package cn.partytime.model.manager.danmuCmdJson;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by administrator on 2017/5/8.
 */
@Document(collection = "cmd_component")
public class CmdComponent extends BaseModel {


    private String id;

    //组件的名称
    private String name;

    //组件的类型 0input 1textarea 2select  3radiobutton 4checkbox
    private Integer type;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
