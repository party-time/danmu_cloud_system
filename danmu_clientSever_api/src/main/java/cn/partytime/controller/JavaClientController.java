package cn.partytime.controller;


import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.*;
import cn.partytime.model.*;
import cn.partytime.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuwei on 2016/8/17.
 */

@RestController
@RequestMapping(value = "/v1/api/javaClient")
@Slf4j
public class JavaClientController {

    @Autowired
    private RpcTimerDanmuService rpcTimerDanmuService;

    @Autowired
    private RpcPartyResourceResultService rpcPartyResourceResultService;

    @Value("${fileUpload.screenSavePath}")
    private String screenSavePath;

    @Autowired
    private RpcUpdatePlanService rpcUpdatePlanService;


    @Autowired
    private RpcParamService rpcParamService;

    @Autowired
    private RpcPromoService rpcPromoService;

    @Autowired
    private RpcDeviceIpInfoService rpcDeviceIpInfoService;

    @Autowired
    private RpcDanmuService rpcDanmuService;

    @Autowired
    private ParamLogicService paramLogicService;

    @Autowired
    private RpcDanmuClientService rpcDanmuClientService;

    @Autowired
    private RpcProjectorService rpcProjectorService;

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;


    @Autowired
    private PartyLogicService partyLogicService;

    @Autowired
    private MovieLogicService movieLogicService;

    /*@Autowired
    private RpcBulbAlarmService rpcBulbAlarmService;*/

    @Autowired
    private TimerDanmuLogicService timerDanmuLogicService;

    @Autowired
    private AdTimerDanmuLogicService adTimerDanmuLogicService;

    @Autowired
    private ResourceLogicService resourceLogicService;

    /**
     * 查找最近的所有的活动
     *
     * @return
     */
    @RequestMapping(value = "/latelyParty", method = RequestMethod.GET)
    public RestResultModel latelyParty(String addressId){
        RestResultModel restResultModel = new RestResultModel();
        DanmuAddressModel danmuAddress = rpcDanmuAddressService.findById(addressId);
        if( null == danmuAddress){
            return restResultModel;
        }
        List<PartyResourceResult> partyResourceResultList1 = null;
        List<PartyResourceResult> partyResourceResultList2 = null;
        //如果是固定场地,下载电影和活动
        if( danmuAddress.getType() == 0){

            partyResourceResultList1 = resourceLogicService.findFilmResource();
            partyResourceResultList2 = resourceLogicService.findPartyResource(addressId);
            //log.info("全部电影"+partyResourceResultList1.size());
            //log.info("该场地下的活动"+partyResourceResultList2.size());
        //如果是临时场地，只下载活动
        }else if( danmuAddress.getType() == 1){
            partyResourceResultList1 = new ArrayList<>();
            partyResourceResultList2 = resourceLogicService.findPartyResource(addressId);
        }
        List<PartyResourceResult> partyResourceResultList = new ArrayList<>();

        partyResourceResultList.addAll(partyResourceResultList1);
        partyResourceResultList.addAll(partyResourceResultList2);


        restResultModel.setData(partyResourceResultList);
        restResultModel.setResult(200);

        return restResultModel;
    }

    @RequestMapping(value = "/findTimerDanmuFile", method = RequestMethod.GET)
    public RestResultModel findTimerDanmuFile(String addressId){
        RestResultModel restResultModel = new RestResultModel();

        restResultModel.setResult(200);
        restResultModel.setData(timerDanmuLogicService.findTimerDanmuFileList());
        return restResultModel;
    }

    @RequestMapping(value = "/findAdTimerDanmu", method = RequestMethod.GET)
    public RestResultModel findAdTimerDanmu(String addressId){
        RestResultModel restResultModel = new RestResultModel();

        restResultModel.setResult(200);
        restResultModel.setData(adTimerDanmuLogicService.findTimerDanmuFileList(addressId));
        return restResultModel;
    }


    @RequestMapping(value = "/findHistoryDanmu", method = RequestMethod.GET)
    public RestResultModel BmsHistoryDanmuService(String partyId,Integer count,Integer clientType,String code,String id){
        //http://localhost:8780/v1/api/javaClient/findHistoryDanmu?partyId=59098703f0b071b95ac9bb7f&time=0&count=200&clientType=0&code=123456
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        restResultModel.setData(rpcDanmuService.findHistoryDanmu(partyId,count,id));
        return restResultModel;
    }

    @RequestMapping(value = "/saveScreen", method = RequestMethod.POST)
    public RestResultModel saveScreen(@RequestParam("Filedata") MultipartFile file) {
        RestResultModel restResultModel = new RestResultModel();
        String targetFilePath = screenSavePath+"/"+file.getOriginalFilename();
        try {
            file.getName();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(targetFilePath)));
            stream.write(file.getBytes());
            stream.close();

        }catch (Exception e){
            restResultModel.setResult(501);
            restResultModel.setResult_msg("失败");
        }
        restResultModel.setResult(200);
        return restResultModel;
    }



    @RequestMapping(value = "/findUpdatePlan", method = RequestMethod.GET)
    public RestResultModel findUpdatePlan(String addressId){
        RestResultModel restResultModel = new RestResultModel();

        restResultModel.setResult(200);

        restResultModel.setData(rpcUpdatePlanService.findByAddressId(addressId));
        return restResultModel;
    }

    @RequestMapping(value = "/updateUpdatePlan", method = RequestMethod.GET)
    public RestResultModel updateUpdatePlan(String id, String result, String machineNum){
        RestResultModel restResultModel = new RestResultModel();
        if("success".equals(result)){
            rpcUpdatePlanService.update(id,1,machineNum);
        }else if("error".equals(result)){
            rpcUpdatePlanService.update(id,2,machineNum);
        }else if("start".equals(result)){
            rpcUpdatePlanService.update(id,3,machineNum);
        }else if("rollback".equals(result)){
            rpcUpdatePlanService.update(id,4,machineNum);
        }
        restResultModel.setResult(200);

        return restResultModel;
    }

    @RequestMapping(value = "/findFlashConfig", method = RequestMethod.GET)
    public RestResultModel findFlashConfig(String code){
        RestResultModel restResultModel = new RestResultModel();
        String objStr = paramLogicService.createJson(rpcParamService.findByRegistCode(code));
        restResultModel.setResult(200);
        restResultModel.setData(objStr);
        return restResultModel;
    }


    @RequestMapping(value = "/danmu-start/{code}/{command}/{clientTime}", method = RequestMethod.GET)
    public RestResultModel partyStart(@PathVariable("code")String code , @PathVariable("command")String command, @PathVariable("clientTime")Long clientTime){
        //验证客户端类型
        return  movieLogicService.partyStart(code,command,clientTime);
    }

    @RequestMapping(value = "/movie-start/{code}/{command}/{partyId}/{clientTime}", method = RequestMethod.GET)
    public RestResultModel filmStart(@PathVariable("code")String code , @PathVariable("command")String command , @PathVariable("partyId")String partyId, @PathVariable("clientTime")Long clientTime){
        //验证客户端是否合法
        return  movieLogicService.moviceStart(partyId,code,clientTime);
    }

    @RequestMapping(value = "/movie-close/{code}/{command}/{partyId}/{clientTime}", method = RequestMethod.GET)
    public RestResultModel filmStop(@PathVariable("code")String code , @PathVariable("command")String command , @PathVariable("partyId")String partyId, @PathVariable("clientTime")Long clientTime){
        RestResultModel restResultModel = new RestResultModel();
        //验证客户端是否合法
        return  movieLogicService.moviceStop(partyId,code,clientTime);
    }
    @RequestMapping(value = "/partyStatus/{code}/{clientType}", method = RequestMethod.GET)
    public RestResultModel partyStatus(@PathVariable("code") String code, @PathVariable("clientType")Integer clientType){
        RestResultModel restResultModel = new RestResultModel();
        //验证客户端是否合法
        return  partyLogicService.partyStatus(code);
    }

    @RequestMapping(value = "/ad-start/{code}/{status}/{name}", method = RequestMethod.GET)
    public RestResultModel adStart(@PathVariable("code") String code, @PathVariable("name")String name, @PathVariable("status")String status){
        RestResultModel restResultModel = new RestResultModel();
        //验证客户端是否合法
        return  rpcPromoService.sendPromoCommand(name,status,code);
    }

    @RequestMapping(value = "/ad-close/{code}/{status}", method = RequestMethod.GET)
    public RestResultModel adStop(@PathVariable("code") String code, @PathVariable("status")String status){
        RestResultModel restResultModel = new RestResultModel();
        //验证客户端是否合法
        return  rpcPromoService.sendPromoCommand(null,status,code);
    }


    /**
     * 投影仪开启
     * @param code
     * @return
     */
    @RequestMapping(value = "/projector-start/{code}", method = RequestMethod.GET)
    public RestResultModel projectorStart(@PathVariable("code") String code){
        RestResultModel restResultModel = new RestResultModel();
        //验证客户端是否合法
        //return  screenService.sendAdStatusToClient(null,status,code);
        DanmuClientModel danmuClient = rpcDanmuClientService.findByRegistCode(code);
        if(danmuClient==null){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("客户端不存在");
            return restResultModel;
        }
        Date nowDate = new Date();
        PageResultModel<ProjectorActionModel> projectorActions =  rpcProjectorService.findProjectorActionPage(code,0,1);
        List<ProjectorActionModel> projectorActionList = projectorActions.getRows();

        if(ListUtils.checkListIsNotNull(projectorActionList)){
            ProjectorActionModel projectorAction = projectorActionList.get(0);
            if(projectorAction.getEndTime()!=null){
                projectorAction = new ProjectorActionModel();
                projectorAction.setStartTime(nowDate);
                projectorAction.setCreateTime(nowDate);
                projectorAction.setUpdateTime(nowDate);
                projectorAction.setRegisterCode(code);
                rpcProjectorService.saveProjectAction(projectorAction);

                restResultModel.setResult(200);
                restResultModel.setResult_msg("OK");
                return restResultModel;
            }else{
                restResultModel.setResult(403);
                restResultModel.setResult_msg("最后一条数据结束时间不为空，此次请求忽略!");
                return restResultModel;
            }
        }else{
            ProjectorActionModel projectorAction = new ProjectorActionModel();
            projectorAction.setStartTime(nowDate);
            projectorAction.setCreateTime(nowDate);
            projectorAction.setUpdateTime(nowDate);
            projectorAction.setRegisterCode(code);
            rpcProjectorService.saveProjectAction(projectorAction);

            restResultModel.setResult(200);
            restResultModel.setResult_msg("OK");
            return restResultModel;
        }
    }

    /**
     * 投影仪关闭
     * @param code
     * @return
     */
    @RequestMapping(value = "/projector-close/{code}", method = RequestMethod.GET)
    public RestResultModel projectorClose(@PathVariable("code") String code){
        RestResultModel restResultModel = new RestResultModel();
        //验证客户端是否合法
        //return  screenService.sendAdStatusToClient(null,status,code);
        DanmuClientModel danmuClient = rpcDanmuClientService.findByRegistCode(code);
        if(danmuClient==null){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("客户端不存在");
            return restResultModel;
        }
        Date nowDate = new Date();
        ProjectorModel projector = rpcProjectorService.findByRegisterCode(code);


        PageResultModel<ProjectorActionModel> projectorActions =  rpcProjectorService.findProjectorActionPage(code,0,1);
        List<ProjectorActionModel> projectorActionList = projectorActions.getRows();
        if(ListUtils.checkListIsNotNull(projectorActionList)){
            ProjectorActionModel projectorAction = projectorActionList.get(0);
            if(projectorAction.getEndTime()==null){
                projectorAction.setUpdateTime(nowDate);
                projectorAction.setEndTime(nowDate);
                projectorAction.setRegisterCode(code);
                rpcProjectorService.saveProjectAction(projectorAction);

                long usedTime = 0;
                Date start = projectorAction.getStartTime();
                long time = nowDate.getTime() - start.getTime();
                usedTime = time + projector.getUsedTime();
                projector.setUsedTime(usedTime);
                projector.setUpdateTime(nowDate);
                rpcProjectorService.saveProjector(projector);

                long usedHour = usedTime/1000/60/60;
                if(usedHour> 2000*0.8){
                    //rpc告警
                    //TODO:
                    //rpcBulbAlarmService.partyStart(code);
                }

                restResultModel.setResult(200);
                restResultModel.setResult_msg("OK");
                return restResultModel;

            }else{
                restResultModel.setResult(403);
                restResultModel.setResult_msg("最后一条数据结束时间不为空，此次请求忽略!");
                return restResultModel;
            }
        }else{
            restResultModel.setResult(405);
            restResultModel.setResult_msg("最后一条数据不存在，此次请求忽略!");
            return restResultModel;
        }
    }




    @RequestMapping(value = "/findDeviceInfo/{addressId}", method = RequestMethod.GET)
    public RestResultModel adStop(@PathVariable("addressId") String addressId){
        RestResultModel restResultModel = new RestResultModel();
        //验证客户端是否合法
        restResultModel.setResult(200);
        restResultModel.setData(rpcDeviceIpInfoService.findByAddressId(addressId));
        return restResultModel;
    }

}
