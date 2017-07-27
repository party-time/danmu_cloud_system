package cn.partytime.model;


import lombok.Data;

import java.util.List;

/**
 * Created by administrator on 2017/5/10.
 */
@Data
public class CmdTempAllData {

    private String id;

    private String name;

    private String key;

    //是否入弹幕库 0入库  1不入库
    private Integer isInDanmuLib;

    //是否发送到H5界面 0 发送 1不发送
    private Integer isSendH5;


    private List<CmdTempComponentData> cmdTempComponentDataList;


}
