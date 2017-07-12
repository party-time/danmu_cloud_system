package cn.partytime.rpc;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.projector.Projector;
import cn.partytime.model.projector.ProjectorAction;
import cn.partytime.service.projector.ProjectorActionService;
import cn.partytime.service.projector.ProjectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Created by dm on 2017/7/12.
 */

@RestController
@RequestMapping("/pcProjector")
public class RpcProjectorService {

    @Autowired
    private ProjectorService projectorService;


    @Autowired
    private ProjectorActionService projectorActionService;


    @RequestMapping(value = "/findByRegisterCode" ,method = RequestMethod.GET)
    public Projector findByRegisterCode(@RequestParam String registorCode) {
        return  projectorService.findByRegisterCode(registorCode);
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
    public PageResultModel<ProjectorAction> findProjectorActionPage(@RequestParam String registorCode, @RequestParam int page, @RequestParam int size){
        Page<ProjectorAction> projectorActionPage = projectorActionService.findProjectorActionPage(registorCode,page,size);
        PageResultModel<ProjectorAction> projectorActionPageResultModel = new PageResultModel<ProjectorAction>();
        projectorActionPageResultModel.setRows(projectorActionPageResultModel.getRows());
        projectorActionPageResultModel.setTotal(projectorActionPageResultModel.getTotal());
        return projectorActionPageResultModel;

    }


}
