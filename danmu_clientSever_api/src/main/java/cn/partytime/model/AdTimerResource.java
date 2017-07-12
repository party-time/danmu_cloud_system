package cn.partytime.model;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */
public class AdTimerResource {

    List<TimerDanmuFileLogicModel> timerDanmuFileLogicModels;


    List<ResourceFile> resourceFileList;

    public List<TimerDanmuFileLogicModel> getTimerDanmuFileLogicModels() {
        return timerDanmuFileLogicModels;
    }

    public void setTimerDanmuFileLogicModels(List<TimerDanmuFileLogicModel> timerDanmuFileLogicModels) {
        this.timerDanmuFileLogicModels = timerDanmuFileLogicModels;
    }

    public List<ResourceFile> getResourceFileList() {
        return resourceFileList;
    }

    public void setResourceFileList(List<ResourceFile> resourceFileList) {
        this.resourceFileList = resourceFileList;
    }
}
