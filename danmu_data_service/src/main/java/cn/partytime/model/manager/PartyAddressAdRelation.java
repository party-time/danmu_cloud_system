package cn.partytime.model.manager;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Administrator on 2017/1/18.
 */
@Document(collection = "party_address_ad_relation")
public class PartyAddressAdRelation extends BaseModel {


    private String id;

    private String partyId;

    private String addressId;

    private String adDanmuPoolId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdDanmuPoolId() {
        return adDanmuPoolId;
    }

    public void setAdDanmuPoolId(String adDanmuPoolId) {
        this.adDanmuPoolId = adDanmuPoolId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
