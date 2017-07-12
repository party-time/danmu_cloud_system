package cn.partytime.model;

import java.util.Date;

public class Projector {

    private String id;

    /**
     * 注册码
     */
    private String registerCode;


    private Date startTime;


    private long usedTime;


    private int realUsedHours;


    private Date updateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getRealUsedHours() {
        return realUsedHours;
    }

    public void setRealUsedHours(int realUsedHours) {
        this.realUsedHours = realUsedHours;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public long getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(long usedTime) {
        this.usedTime = usedTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
