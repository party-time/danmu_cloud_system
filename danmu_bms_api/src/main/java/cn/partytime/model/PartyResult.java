package cn.partytime.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuwei on 2016/10/28.
 */
public class PartyResult {

    private String id;

    private String name;

    private Date startTime;

    private Date endTime;

    private String shortName;

    /**
     * 活动关联的弹幕库
     */
    private String danmuLibraryId = "0";


    private Integer type;

    private Integer status;

    private String movieAlias;


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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDanmuLibraryId() {
        return danmuLibraryId;
    }

    public void setDanmuLibraryId(String danmuLibraryId) {
        this.danmuLibraryId = danmuLibraryId;
    }

    public String getStartTimeStr() {
        if( null != this.getStartTime()){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.getStartTime());
        }else{
            return null;
        }

    }

    public String getEndTimeStr() {
        if( null != this.getEndTime()){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.getEndTime());
        }else{
            return null;
        }

    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMovieAlias() {
        return movieAlias;
    }

    public void setMovieAlias(String movieAlias) {
        this.movieAlias = movieAlias;
    }
}
