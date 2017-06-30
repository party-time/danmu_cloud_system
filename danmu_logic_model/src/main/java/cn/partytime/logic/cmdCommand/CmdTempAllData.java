package cn.partytime.logic.cmdCommand;

import java.util.List;

/**
 * Created by administrator on 2017/5/10.
 */
public class CmdTempAllData {

    private String id;

    private String name;

    private String key;

    //是否入弹幕库 0入库  1不入库
    private Integer isInDanmuLib;

    //是否发送到H5界面 0 发送 1不发送
    private Integer isSendH5;


    private List<CmdTempComponentData> cmdTempComponentDataList;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<CmdTempComponentData> getCmdTempComponentDataList() {
        return cmdTempComponentDataList;
    }

    public void setCmdTempComponentDataList(List<CmdTempComponentData> cmdTempComponentDataList) {
        this.cmdTempComponentDataList = cmdTempComponentDataList;
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
}
