package cn.partytime.model.manager;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lENOVO on 2016/9/18.
 */
public class Location implements Serializable{

    private static final long serialVersionUID = -200163941696289983L;
    /**
     * 类型
     */
    private String type;

    /**
     * 经纬度
     */
    private List<Double> coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
