package cn.partytime.message.model;

import lombok.Data;

/**
 * Created by liuwei on 16/6/12.
 * 弹幕活动的地址
 */

@Data
public class DanmuAddress {

    private String id;

    //地址别名
    private String name;

    //场地类型 0 固定场地  1 临时场地
    private Integer type;
}
