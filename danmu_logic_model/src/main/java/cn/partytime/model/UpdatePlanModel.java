package cn.partytime.model;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class UpdatePlanModel {
    private String id;

    private String addressId;

    private String versionId;

    private Date updatePlanTime;

    private List<UpdatePlanMachineModel> updatePlanMachineList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

}
