package cn.partytime.model.danmu;

import cn.partytime.baseModel.BaseModel;

/**
 * Created by task on 16/6/24.
 */
public class BasePool extends BaseModel {
    /**
     *  活动编号
     */
    private String partyId;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

}
