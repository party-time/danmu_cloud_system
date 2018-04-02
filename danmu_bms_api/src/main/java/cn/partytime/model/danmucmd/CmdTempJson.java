package cn.partytime.model.danmucmd;

import cn.partytime.model.manager.danmuCmdJson.CmdJsonParam;

import java.util.List;

/**
 * Created by administrator on 2017/5/8.
 */
public class CmdTempJson {

    private String cmdTempId;

    private String tempName;

    private String key;

    //是否进入弹幕库 0入库  1不入库
    private Integer isInDanmuLib;

    //是否发送到H5 0 发送 1不发送
    private Integer isSendH5;

    //排序
    private Integer sort;

    //是否显示
    private Integer show;

    private Integer type;

    private List<CmdJsonParam> cmdJsonParamList;

    public String getTempName() {
        return tempName;
    }

    public String getCmdTempId() {
        return cmdTempId;
    }

    public void setCmdTempId(String cmdTempId) {
        this.cmdTempId = cmdTempId;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<CmdJsonParam> getCmdJsonParamList() {
        return cmdJsonParamList;
    }

    public void setCmdJsonParamList(List<CmdJsonParam> cmdJsonParamList) {
        this.cmdJsonParamList = cmdJsonParamList;
    }

    public Integer getIsInDanmuLib() {
        return isInDanmuLib;
    }

    public void setIsInDanmuLib(Integer isInDanmuLib) {
        this.isInDanmuLib = isInDanmuLib;
    }

    public Integer getIsSendH5() {
        return isSendH5;
    }

    public void setIsSendH5(Integer isSendH5) {
        this.isSendH5 = isSendH5;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getShow() {
        return show;
    }

    public void setShow(Integer show) {
        this.show = show;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
