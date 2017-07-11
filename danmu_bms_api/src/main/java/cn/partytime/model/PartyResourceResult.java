package cn.partytime.model;

import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.ResourceFile;

import java.util.List;

/**
 * Created by liuwei on 2016/9/12.
 * 用于表明活动与资源文件的关联
 */
public class PartyResourceResult {

    private Party party;

    private List<ResourceFile> resourceFileList;

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public List<ResourceFile> getResourceFileList() {
        return resourceFileList;
    }

    public void setResourceFileList(List<ResourceFile> resourceFileList) {
        this.resourceFileList = resourceFileList;
    }
}
