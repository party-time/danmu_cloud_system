package cn.partytime.rpcService.dataRpcService.impl;

import cn.partytime.rpcService.dataRpcService.ProjectorService;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.Projector;
import cn.partytime.model.ProjectorAction;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/11.
 */

@Component
public class ProjectorServiceHystrix implements ProjectorService {
    @Override
    public Projector findByRegisterCode(String registorCode) {
        return null;
    }

    @Override
    public void saveProjectAction(ProjectorAction projectorAction) {

    }

    @Override
    public void saveProjector(Projector projector) {

    }

    @Override
    public PageResultModel<ProjectorAction> findProjectorActionPage(String registorCode, int page, int size) {
        return null;
    }
}
