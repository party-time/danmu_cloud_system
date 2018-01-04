package cn.partytime.model.spider;

import cn.partytime.baseModel.BaseModel;

import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 2017/12/25.
 */
public class Spider extends BaseModel{

    private String id;

    //来源：0豆瓣
    private Integer source;

    //原始id
    private String oldId;

    //电影名称
    private String name;

    //电影海报
    private String imageUrl;

    //0正在播放  1即将播放
    private Integer status=0;

    //电影时长
    private Integer time;

    //字符串的日期
    private String dateStr;

    //上映时间
    private Date date;

    //评分
    private String score;

    //标签
    private List<String> typeList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}
