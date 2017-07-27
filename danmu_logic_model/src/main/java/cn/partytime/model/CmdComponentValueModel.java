package cn.partytime.model;


import lombok.Data;

@Data
public class CmdComponentValueModel {

    private String id;

    //组件的id
    private String componentId;

    //组件值的名称
    private String name;

    //组件值的值
    private String value;


}
