package cn.partytime.model;

import lombok.Data;

import java.util.List;

/**
 * Created by administrator on 2017/5/10.
 */

@Data
public class CmdTempComponentData {

    private String id;

    //组件的id 0无组件 1表情 2特效图片
    private String  componentId;


    //组件的类型 0text 1textarea 2select  3radiobutton 4checkbox
    private Integer componentType;

    private List<CmdComponentValueModel> cmdComponentValueList;

    //用于生成json体的key
    private String key;

    //参数的默认值
    private String defaultValue;

    //是否出现在审核界面 0出现 1不出现
    private Integer isCheck;

    //字段排序
    private Integer sort;

    //校验规则
    private String checkRule;

    //0数字 1布尔值 2字符串 3数组
    private Integer type;

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


}
