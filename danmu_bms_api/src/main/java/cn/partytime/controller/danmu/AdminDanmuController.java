package cn.partytime.controller.danmu;

import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.rpcService.CmdLogicService;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.danmuCmdJson.CmdTemp;
import cn.partytime.service.BmsDanmuService;
import cn.partytime.service.danmuCmdJson.CmdTempService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dm on 2017/5/19.
 */


@RestController
@RequestMapping(value = "/v1/api/admin")
public class AdminDanmuController  extends BaseAdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminDanmuController.class);

    @Autowired
    private CmdTempService cmdTempService;


    @Autowired
    private CmdLogicService cmdLogicService;


    @Autowired
    private BmsDanmuService bmsDanmuService;



    @RequestMapping(value = "/findDanmuType", method = RequestMethod.GET)
    public RestResultModel findDanmuType(Integer pageNumber, Integer pageSize) {

        logger.info("pageNumber:{},pageSize:{}",pageNumber,pageSize);
        RestResultModel restResultModel = new RestResultModel();
        Page<CmdTemp> cmdTempPage = cmdTempService.findAll(pageNumber-1,pageSize);
        restResultModel.setResult(200);
        restResultModel.setData(cmdTempPage);
        return restResultModel;
    }

    @RequestMapping(value = "/findDanmuTemplateInfo/{id}", method = RequestMethod.GET)
    public RestResultModel findDanmuTemplateInfo(@PathVariable("id")String id){

        RestResultModel restResultModel = new RestResultModel();
        CmdTempAllData cmdTempAllData = cmdLogicService.findCmdTempAllDataByIdFromCache(id);
        restResultModel.setResult(200);
        restResultModel.setData(cmdTempAllData);
        return restResultModel;
    }

    @RequestMapping(value = "/danmuSend", method = RequestMethod.POST)
    public RestResultModel findDanmuTemplateInfo(HttpServletRequest request){

        RestResultModel restResultModel = new RestResultModel();
        restResultModel =bmsDanmuService.sendDanmuByAdmin(request,getAdminUser().getId(),0,0);
        return restResultModel;
    }



}
