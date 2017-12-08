package cn.partytime.rpc;

import cn.partytime.cache.projector.ProjectorAlarmCacheService;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.model.DanmuClientModel;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.ProjectorActionModel;
import cn.partytime.model.ProjectorModel;
import cn.partytime.model.client.DanmuClient;
import cn.partytime.model.projector.Projector;
import cn.partytime.model.projector.ProjectorAction;
import cn.partytime.service.DanmuClientService;
import cn.partytime.service.projector.ProjectorActionService;
import cn.partytime.service.projector.ProjectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dm on 2017/7/12.
 */

@RestController
@RequestMapping("/rpcProjector")
@Slf4j
public class RpcProjectorService {

    @Autowired
    private ProjectorService projectorService;


    @Autowired
    private ProjectorActionService projectorActionService;

    @Autowired
    private DanmuClientService danmuClientService;


    @Autowired
    private ProjectorAlarmCacheService projectorAlarmCacheService;


    @RequestMapping(value = "/findByRegisterCode" ,method = RequestMethod.GET)
    public ProjectorModel findByRegisterCode(@RequestParam String registorCode) {
        log.info("通过注册码获取投影仪信息");
        Projector projector =  projectorService.findByRegisterCode(registorCode);
        ProjectorModel projectorModel = new ProjectorModel();
        if(projector!=null){
            BeanUtils.copyProperties(projector,projectorModel);
            return projectorModel;
        }
        return  null;
    }


    @RequestMapping(value = "/saveProjectAction" ,method = RequestMethod.POST)
    public void saveProjectAction(@RequestBody ProjectorAction projectorAction){
        projectorActionService.save(projectorAction);
    }

    @RequestMapping(value = "/saveProjector" ,method = RequestMethod.POST)
    public void saveProjector(@RequestBody Projector projector){
        projectorService.save(projector);
    }


    @RequestMapping(value = "/findProjectorActionPage" ,method = RequestMethod.GET)
    public PageResultModel<ProjectorActionModel> findProjectorActionPage(@RequestParam String registorCode, @RequestParam int page, @RequestParam int size){
        Page<ProjectorAction> projectorActionPage = projectorActionService.findProjectorActionPage(registorCode,page,size);


        PageResultModel<ProjectorActionModel> projectorActionPageResultModel = new PageResultModel<ProjectorActionModel>();
        projectorActionPageResultModel.setTotal(projectorActionPage.getTotalElements());
        List<ProjectorAction> projectorActionList =  projectorActionPage.getContent();

        List<ProjectorActionModel> projectorActionModelList = new ArrayList<>();
        projectorActionList = projectorActionPage.getContent();
        if(ListUtils.checkListIsNotNull(projectorActionList)){
            for(ProjectorAction projectorAction:projectorActionList){
                ProjectorActionModel projectorActionModel = new ProjectorActionModel();
                BeanUtils.copyProperties(projectorAction,projectorActionModel);
                projectorActionModelList.add(projectorActionModel);
            }
        }
        projectorActionPageResultModel.setRows(projectorActionModelList);
        return projectorActionPageResultModel;

    }


}
