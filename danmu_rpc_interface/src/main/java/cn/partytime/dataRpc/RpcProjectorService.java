package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcProjectorServiceHystrix;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.ProjectorActionModel;
import cn.partytime.model.ProjectorModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/12.
 */

@FeignClient(value = "${dataRpcServer}",fallback = RpcProjectorServiceHystrix.class)
public interface RpcProjectorService {

    @RequestMapping(value = "/rpcProjector/findByRegisterCode" ,method = RequestMethod.GET)
    public ProjectorModel findByRegisterCode(@RequestParam(value = "registorCode") String registorCode);

    @RequestMapping(value = "/rpcProjector/saveProjectAction" ,method = RequestMethod.POST)
    public void saveProjectAction(ProjectorActionModel projectorActionModel);

    @RequestMapping(value = "/rpcProjector/saveProjector" ,method = RequestMethod.POST)
    public void saveProjector(ProjectorModel projectorModel);

    @RequestMapping(value = "/rpcProjector/findProjectorActionPage" ,method = RequestMethod.GET)
    public PageResultModel<ProjectorActionModel> findProjectorActionPage(@RequestParam(value = "registorCode") String registorCode, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size);

}
