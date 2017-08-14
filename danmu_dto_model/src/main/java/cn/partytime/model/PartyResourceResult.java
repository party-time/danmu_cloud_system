package cn.partytime.model;

import lombok.Data;

import java.util.List;

/**
 * Created by liuwei on 2016/9/12.
 * 用于表明活动与资源文件的关联
 */

@Data
public class PartyResourceResult {

    private PartyModel party;

    private List<ResourceFileModel> resourceFileList;
}
