package cn.partytime.model.danmucmd;

import cn.partytime.model.manager.danmuCmdJson.CmdComponentValue;

import java.util.List;

/**
 * Created by administrator on 2017/5/9.
 */
public class CmdComponentJson {

    private String componentId;

    private String name;

    private Integer type;

    private List<CmdComponentValue> cmdComponentValueList;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<CmdComponentValue> getCmdComponentValueList() {
        return cmdComponentValueList;
    }

    public void setCmdComponentValueList(List<CmdComponentValue> cmdComponentValueList) {
        this.cmdComponentValueList = cmdComponentValueList;
    }
}
