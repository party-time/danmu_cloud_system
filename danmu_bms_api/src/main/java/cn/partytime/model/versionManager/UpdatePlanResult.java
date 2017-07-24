package cn.partytime.model.versionManager;

import cn.partytime.model.manager.UpdatePlan;
import cn.partytime.model.manager.Version;

/**
 * Created by administrator on 2017/2/14.
 */
public class UpdatePlanResult {

    private UpdatePlan updatePlan;

    private Version version;

    public UpdatePlan getUpdatePlan() {
        return updatePlan;
    }

    public void setUpdatePlan(UpdatePlan updatePlan) {
        this.updatePlan = updatePlan;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }
}
