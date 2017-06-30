package cn.partytime.logic.danmu;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lENOVO on 2016/11/24.
 */
public class ProtocolCommandModel implements Serializable {

    private static final long serialVersionUID = -1763062384302601946L;

    /**命令类型*/
    private String type;

    /**命令内容*/
    private String status;

    /**弹幕方位*/
    private String direction;


    /**活动编号*/
    private String partyId;

    /**
     * 活动开始时间
     */
    private Long partyTime;

    /**
     * 电影开始时间
     */
    private Long movieTime;
    /**时间*/
    private Long  time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getPartyTime() {
        return partyTime;
    }

    public void setPartyTime(Long partyTime) {
        this.partyTime = partyTime;
    }

    public Long getMovieTime() {
        return movieTime;
    }

    public void setMovieTime(Long movieTime) {
        this.movieTime = movieTime;
    }
}
