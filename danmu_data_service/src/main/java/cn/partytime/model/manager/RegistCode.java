package cn.partytime.model.manager;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by liuwei on 2016/9/7.
 * 注册码
 */
public class RegistCode {


    private String id;

    private String registCode;

    //注册码的类型，0有时效  1永久有效
    private Integer type;

    //注册码失效期
    private Date overdue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistCode() {
        return registCode;
    }

    public void setRegistCode(String registCode) {
        this.registCode = registCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getOverdue() {
        return overdue;
    }

    public void setOverdue(Date overdue) {
        this.overdue = overdue;
    }
}
