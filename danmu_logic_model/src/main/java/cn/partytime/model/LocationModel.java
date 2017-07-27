package cn.partytime.model;

import lombok.Data;

import java.util.List;


@Data
public class LocationModel {

    /**
     * 类型
     */
    private String type;
    /**
     * 经纬度
     */
    private List<Double> coordinates;

}
