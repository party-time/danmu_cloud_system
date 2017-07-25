package cn.partytime.model;

import java.util.List;

public class PartyResourceResult {

    private PartyDTO party;

    private List<ResourceFile> resourceFileList;

    public PartyDTO getParty() {
        return party;
    }

    public void setParty(PartyDTO party) {
        this.party = party;
    }

    public List<ResourceFile> getResourceFileList() {
        return resourceFileList;
    }

    public void setResourceFileList(List<ResourceFile> resourceFileList) {
        this.resourceFileList = resourceFileList;
    }
}