package cn.partytime.model;

import java.util.List;

/**
 * Created by administrator on 2017/9/5.
 */
public class TimerDanmuPathModel {

    private String partyId;

    private List<String> pathList;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }
}
