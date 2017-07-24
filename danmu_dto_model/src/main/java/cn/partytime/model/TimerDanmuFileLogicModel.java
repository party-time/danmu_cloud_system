package cn.partytime.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/28.
 */
public class TimerDanmuFileLogicModel implements Serializable {

    private String id;


    private String path;


    private String partyId;

    private String poolId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }
}
