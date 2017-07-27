package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcProjectorService;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.ProjectorActionModel;
import cn.partytime.model.ProjectorModel;
import org.springframework.stereotype.Component;


@Component
public class RpcProjectorServiceHystrix implements RpcProjectorService {
    @Override
    public ProjectorModel findByRegisterCode(String registorCode) {
        return null;
    }

    @Override
    public void saveProjectAction(ProjectorActionModel projectorActionModel) {

    }

    @Override
    public void saveProjector(ProjectorModel projectorModel) {

    }

    @Override
    public PageResultModel<ProjectorActionModel> findProjectorActionPage(String registorCode, int page, int size) {
        return null;
    }
}
