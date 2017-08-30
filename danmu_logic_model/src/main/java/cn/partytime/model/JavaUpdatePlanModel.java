package cn.partytime.model;

import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 2017/2/17.
 */
public class JavaUpdatePlanModel {

    private String id;

    private String version;

    private Integer type;

    private Date updateDate;

    private List<UpdatePlanMachineModel> updatePlanMachineList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public List<UpdatePlanMachineModel> getUpdatePlanMachineList() {
        return updatePlanMachineList;
    }

    public void setUpdatePlanMachineList(List<UpdatePlanMachineModel> updatePlanMachineList) {
        this.updatePlanMachineList = updatePlanMachineList;
    }
}
