package cn.partytime.model;

import java.util.List;

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