package cn.partytime.logic.danmu;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/7 0007.
 */
public class PromotionalFilmMode implements Serializable {

    private String name;

    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
