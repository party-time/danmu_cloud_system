package cn.partytime.model.wechat;

import java.util.Date;

/**
 * Created by administrator on 2017/4/7.
 */
public class MaterlResultJson {

    private String media_id;

    private String name;

    private Date update_time;

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

}
