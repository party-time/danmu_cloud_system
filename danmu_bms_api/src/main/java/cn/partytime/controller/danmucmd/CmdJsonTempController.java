package cn.partytime.controller.danmucmd;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.danmucmd.CmdComponentJson;
import cn.partytime.model.danmucmd.CmdTempJson;
import cn.partytime.model.manager.danmuCmdJson.CmdComponent;
import cn.partytime.model.manager.danmuCmdJson.CmdTemp;
import cn.partytime.service.danmuCmd.BmsCmdService;
import cn.partytime.service.danmuCmdJson.CmdJsonComponentService;
import cn.partytime.service.danmuCmdJson.CmdTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by administrator on 2017/5/8.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/cmdTemp")
@Slf4j
public class CmdJsonTempController {

    @Autowired
    private CmdTempService cmdTempService;

    @Autowired
    private BmsCmdService bmsCmdService;

    @Autowired
    private CmdJsonComponentService cmdJsonComponentService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel paramTemplatePage(Integer pageNumber , Integer pageSize ){
        pageNumber = pageNumber-1;
        Page<CmdTemp> cmdJsonTempPage = cmdTempService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(cmdJsonTempPage.getTotalElements());
        pageResultModel.setRows(cmdJsonTempPage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST,consumes = "application/json")
    public RestResultModel save(@RequestBody(required = false)CmdTempJson cmdTempJson){
        RestResultModel restResultModel = new RestResultModel();
        if( null == cmdTempJson){
            restResultModel.setResult(500);
            return restResultModel;
        }
        if(StringUtils.isEmpty(cmdTempJson.getCmdTempId())){
            Integer count = cmdTempService.countByKey(cmdTempJson.getKey());
            if( count > 0){
                restResultModel.setResult(501);
                return restResultModel;
            }
        }else{
            Integer count = cmdTempService.countByKey(cmdTempJson.getKey());
            if( count > 1){
                restResultModel.setResult(501);
                return restResultModel;
            }
        }

        bmsCmdService.save(cmdTempJson);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();

        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/checkKey", method = RequestMethod.GET)
    public RestResultModel checkKey(String key){
        RestResultModel restResultModel = new RestResultModel();
        Integer count = cmdTempService.countByKey(key);
        if( count > 0){
            restResultModel.setResult(501);
            return restResultModel;
        }else {
            restResultModel.setResult(200);
            return restResultModel;
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public RestResultModel find(String id){
        RestResultModel restResultModel = new RestResultModel();
        CmdTempJson cmdTempJson = bmsCmdService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(cmdTempJson);
        return restResultModel;
    }




    @RequestMapping(value = "/saveComponent", method = RequestMethod.POST,consumes = "application/json")
    public RestResultModel saveComponet(@RequestBody(required = false)CmdComponentJson cmdComponentJson){
        RestResultModel restResultModel = new RestResultModel();
        bmsCmdService.saveComponent(cmdComponentJson);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/componentPage", method = RequestMethod.GET)
    public PageResultModel componentPage(Integer pageNumber , Integer pageSize ){
        pageNumber = pageNumber-1;
        Page<CmdComponent> cmdJsonTempPage = cmdJsonComponentService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel  = new PageResultModel();
        pageResultModel.setTotal(cmdJsonTempPage.getTotalElements());
        pageResultModel.setRows(cmdJsonTempPage.getContent());

        return pageResultModel;
    }

    @RequestMapping(value = "/delComponent", method = RequestMethod.GET)
    public RestResultModel delComponent(String id){
        RestResultModel restResultModel = new RestResultModel();
        cmdJsonComponentService.delById(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/findAllComponent", method = RequestMethod.GET)
    public RestResultModel findAllComponent(){
        RestResultModel restResultModel = new RestResultModel();
        List<CmdComponent> cmdComponentList =cmdJsonComponentService.findAll();
        restResultModel.setResult(200);
        restResultModel.setData(cmdComponentList);
        return restResultModel;
    }

    @RequestMapping(value = "/findComponentById", method = RequestMethod.GET)
    public RestResultModel findComponentById(String id){
        RestResultModel restResultModel = new RestResultModel();
        CmdComponentJson cmdComponentJson = bmsCmdService.findCmdComponentJsonById(id);
        restResultModel.setResult(200);
        restResultModel.setData(cmdComponentJson);
        return restResultModel;
    }

}
