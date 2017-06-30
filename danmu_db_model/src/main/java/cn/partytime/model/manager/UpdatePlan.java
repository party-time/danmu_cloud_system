package cn.partytime.model.manager;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 2017/2/13.
 */
@Document(collection = "update_plan")
public class UpdatePlan extends BaseModel {

    @Field("_id")
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
