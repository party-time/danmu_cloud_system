package cn.partytime.model;

import java.util.Date;
import java.util.List;

public class UpdatePlan {

    private String id;

    private String addressId;

    private String versionId;

    private Date updatePlanTime;

    private List<UpdatePlanMachine> updatePlanMachineList;

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

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Date getUpdatePlanTime() {
        return updatePlanTime;
    }

    public void setUpdatePlanTime(Date updatePlanTime) {
        this.updatePlanTime = updatePlanTime;
    }

    public List<UpdatePlanMachine> getUpdatePlanMachineList() {
        return updatePlanMachineList;
    }

    public void setUpdatePlanMachineList(List<UpdatePlanMachine> updatePlanMachineList) {
        this.updatePlanMachineList = updatePlanMachineList;
    }
}