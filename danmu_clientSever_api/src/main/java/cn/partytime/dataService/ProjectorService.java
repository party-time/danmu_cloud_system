package cn.partytime.dataService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.dataService.impl.DanmuAddressServiceHystrix;
import cn.partytime.dataService.impl.ProjectorServiceHystrix;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.Projector;
import cn.partytime.model.ProjectorAction;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/11.
 */
@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = ProjectorServiceHystrix.class)
public interface ProjectorService {

    @RequestMapping(value = "/rpcProjector/findByRegisterCode" ,method = RequestMethod.GET)
    public Projector findByRegisterCode(@RequestParam(value = "registorCode")String registorCode);


    @RequestMapping(value = "/rpcProjector/saveProjectAction" ,method = RequestMethod.POST)
    public void saveProjectAction(ProjectorAction projectorAction);

    @RequestMapping(value = "/rpcProjector/saveProjector" ,method = RequestMethod.POST)
    public void saveProjector(Projector projector);

    @RequestMapping(value = "/rpcProjector/findProjectorActionPage" ,method = RequestMethod.GET)
    public PageResultModel<ProjectorAction> findProjectorActionPage(@RequestParam(value = "registorCode") String registorCode, @RequestParam(value = "page")int page, @RequestParam(value = "size") int size);
}
