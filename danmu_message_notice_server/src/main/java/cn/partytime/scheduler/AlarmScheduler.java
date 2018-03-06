package cn.partytime.scheduler;

import cn.partytime.cache.admin.CheckAdminCacheService;
import cn.partytime.cache.collector.CollectorCacheService;
import cn.partytime.cache.projector.ProjectorAlarmCacheService;
import cn.partytime.cache.projector.ProjectorCacheService;
import cn.partytime.common.cachekey.client.ClientCommandCacheKey;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.*;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.logicService.MessageLogicService;
import cn.partytime.model.*;
import cn.partytime.redis.service.RedisService;
import cn.partytime.rpc.RpcAdminAlarmService;
import cn.partytime.rpc.RpcClientAlarmService;
import cn.partytime.rpc.RpcMovieAlarmService;
import cn.partytime.rpc.RpcProjectorAlarmService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@EnableScheduling
@RefreshScope
@Slf4j
public class AlarmScheduler {

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private CollectorCacheService collectorCacheService;

    @Autowired
    private RpcMovieScheduleService rpcMovieScheduleService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RpcDanmuClientService rpcDanmuClientService;

    @Autowired
    private CheckAdminCacheService checkAdminCacheService;

    @Autowired
    private RpcAdminAlarmService rpcAdminAlarmService;

    @Autowired
    private RpcClientAlarmService rpcClientAlarmService;

    @Autowired
    private RpcMovieAlarmService rpcMovieAlarmService;

    @Autowired
    private ProjectorAlarmCacheService projectorAlarmCacheService;

    @Autowired
    private ProjectorCacheService projectorCacheService;

    @Autowired
    private RpcTimerDanmuService rpcTimerDanmuService;

    @Value("${alarm.admin.offlineTime}")
    private int clientOfflineTime;

    @Value("${client.online.count}")
    private int clientOnlineCount;


    @Value("${server.env:1}")
    private int env;


    /**
     * 投影未开启告警计数清0
     */
    /*@Scheduled(cron = "0 0 6 * * ?")
    private void projectorClearProjectorAlarmCountCache(){
        log.info("-----------6点执行清除投影仪告警计数缓存----------------------");
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)) {
            for (DanmuAddressModel danmuAddress : danmuAddressList) {
                String addressId = danmuAddress.getId();
                log.info("场地:{}投影未开启告警计数清0",danmuAddress.getName());
                List<DanmuClientModel>  danmuClientList = rpcDanmuClientService.findByAddressId(addressId);
                if(ListUtils.checkListIsNotNull(danmuClientList)){
                    for(DanmuClientModel danmuClient:danmuClientList){
                        String regsitrorCode = danmuClient.getRegistCode();
                        projectorAlarmCacheService.removeAlarmAllCache(addressId,regsitrorCode);
                    }
                }
            }
        }
    }*/


    /**
     * 清除投影仪关闭计数缓存
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void projectorA(){
        log.info("-----------0点清除投影仪关闭计数缓存----------------------");
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)) {
            for (DanmuAddressModel danmuAddress : danmuAddressList) {
                String addressId = danmuAddress.getId();
                projectorCacheService.clearProjectCloseCount(addressId);
            }
        }
    }

    /**
     * 关闭所有场地的投影
     */
    @Scheduled(cron = "0 0 3 * * ?")
    private void projectorCloseScheduled(){
        log.info("-------------关闭所有场地的投影仪-------------");
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)) {
            for (DanmuAddressModel danmuAddress : danmuAddressList) {
                String addressId = danmuAddress.getId();
                PartyLogicModel partyLogicModel = rpcPartyService.findPartyByAddressId(addressId);
                if(partyLogicModel!=null){
                    log.info("当前场地有活动正在进行，不关闭投影");
                    //获取类型 0:活动场：1：弹幕场
                    String partyId = partyLogicModel.getPartyId();
                    if(partyLogicModel.getType()==0){
                        continue;
                    }
                }else{
                    Object object = projectorCacheService.getProjectCloseCount(addressId);
                    if(object!=null){
                         continue;
                    }

                    log.info("关闭场地:{}投影",danmuAddress.getName());
                    sendCommand("projectorClose",addressId,"");
                    projectorCacheService.setProjectCloseCount(addressId);
                }
            }
        }
    }
    /**
     * 投影未关闭告警
     */
    /*@Scheduled(cron = "0 5 2 * * ?")
    private void projectorCloseCommandListener(){
        log.info("投影关闭监听逻辑");
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddressModel danmuAddress:danmuAddressList){
                String addressId = danmuAddress.getId();
                log.info("检测场地{}，投影{}是否收到Tms发出的指令",addressId,danmuAddress.getName());
                List<DanmuClientModel>  danmuClientList = rpcDanmuClientService.findByAddressId(addressId);
                if(ListUtils.checkListIsNotNull(danmuClientList)){
                    for(DanmuClientModel danmuClient:danmuClientList){
                        String regsitrorCode = danmuClient.getRegistCode();
                        PageResultModel<ProjectorActionModel> projectorActions =  rpcProjectorService.findProjectorActionPage(regsitrorCode,0,1);
                        List<ProjectorActionModel> projectorActionList = projectorActions.getRows();
                        if(ListUtils.checkListIsNotNull(projectorActionList)){
                            ProjectorActionModel projectorAction = projectorActionList.get(0);
                            if(projectorAction.getEndTime()==null && DateUtils.checkDateIsBeforeCurrentDate(projectorAction.getCreateTime())){
                                rpcProjectorAlarmService.projectorClose(regsitrorCode);
                            }
                        }
                    }
                }
            }
        }

    }*/

    @Scheduled(cron = "0/30 * * * * ?")
    public void checkAdminOffLineScheduler() {

        log.info("管理员离线告警监听");
        //获取管理员掉县时间
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            int type = 1;
            for(DanmuAddressModel danmuAddressModel:danmuAddressList){
                //rpcPartyService.findPartyAddressId(danmuAddressModel.getId());
                PartyLogicModel partyLogicModel = rpcPartyService.findFilmByAddressId(danmuAddressModel.getId());
                if(partyLogicModel!=null){
                    String partyId = partyLogicModel.getPartyId();
                    String addressId = partyLogicModel.getAddressId();
                    int count = checkAdminCacheService.getCheckAdminCount(type);
                    long offlineTime = checkAdminCacheService.findAdminOfflineTime(type);
                    if(count==0){
                        if(offlineTime==0){
                            MovieScheduleModel movieScheduleModel = rpcMovieScheduleService.findCurrentMovie(partyId,addressId);
                            if(movieScheduleModel!=null){
                                Date movieStartTime = movieScheduleModel.getStartTime();
                                log.info("movieStartTime:{}",movieStartTime);
                                rpcAdminAlarmService.admiOffLineAlarm(movieStartTime.getTime());
                            }
                        }else{
                            rpcAdminAlarmService.admiOffLineAlarm(offlineTime);
                        }

                    }
                    break;
                }
            }
        }
    }

    @Scheduled(cron = "0 30 9 * * ?")
    public void flashOnlineLinstener() {
        if(env==0){
            return;
        }
        List<DanmuAddressModel> danmuAddressModels = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressModels)) {
            for (DanmuAddressModel danmuAddressModel : danmuAddressModels) {
                String addressId = danmuAddressModel.getId();
                int count  = collectorCacheService.getClientCount(0,addressId);
                log.info("-----------------------当前场地flash数量:{}",count);
                if(count<clientOnlineCount){
                    log.info("----------------------flash从来未启动过");
                    rpcClientAlarmService.clientNetError(addressId);
                }
            }
        }
    }

   @Scheduled(cron = "0 0/1 * * * ?")
    public void clientOffLineScheduler() {
        log.info("客户端在线数量监听");
        List<DanmuAddressModel> danmuAddressModels = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressModels)) {
            for (DanmuAddressModel danmuAddressModel : danmuAddressModels) {
                PartyLogicModel partyLogicModel = rpcPartyService.findFilmByAddressId(danmuAddressModel.getId());
                if(partyLogicModel!=null){
                    String partyId = partyLogicModel.getPartyId();
                    String addressId = partyLogicModel.getAddressId();
                    int count  = collectorCacheService.getClientCount(0,addressId);
                    log.info("当前场地flash数量:{}",count);
                    if(count<clientOnlineCount){
                        long time = collectorCacheService.findFlashOfflineTime(addressId);
                        log.info("flash离线的具体时间:{}",time);
                        Date date =  DateUtils.getCurrentDate();
                        long subTime = date.getTime() - time;
                        long minute = subTime/1000/60;
                        log.info("flash 离线时间:{}",minute);
                        if(time==0){
                            log.info("flash从来未启动过");
                            rpcClientAlarmService.clientNetError(addressId);
                        }else if(minute>=clientOfflineTime){
                            log.info("flash离线超过制定的时间{},{}",minute,clientOfflineTime);
                            rpcClientAlarmService.clientNetError(addressId);
                        }
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void projectorCloaseScheduler() {

        log.info("执行投影关闭定时任务---------------start");
        int hour = DateUtils.getCurrentHour();
        if(hour >7){
            log.info("当前时间大于7点逻辑终止");
            return;
        }
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddressModel danmuAddress:danmuAddressList){
                String addressId = danmuAddress.getId();

                Object object = projectorCacheService.getProjectCloseCount(addressId);
                if(object!=null){
                    continue;
                }
                PartyLogicModel partyLogicModel = rpcPartyService.findFilmByAddressId(addressId);
                if(partyLogicModel!=null){
                    String partyId = partyLogicModel.getPartyId();
                    List<MovieScheduleModel>  movieScheduleList =  rpcMovieScheduleService.findByPartyIdAndAddressId(partyId,addressId);
                    if(ListUtils.checkListIsNotNull(movieScheduleList)){
                        MovieScheduleModel movieSchedule = movieScheduleList.get(0);

                        if(movieSchedule.getEndTime()!=null){
                            //关闭投影指令
                            log.info("结束时间不为空，直接发出投影关闭指令");
                            sendCommand("projectorClose",addressId,"");
                            projectorCacheService.setProjectCloseCount(addressId);

                        }else if(movieSchedule.getMoviceStartTime()!=null){
                            Date tempDate = movieSchedule.getStartTime();
                            List<TimerDanmuModel>  timerDanmuModelList = rpcTimerDanmuService.findByPartyIdOrderBytimeDesc(partyId,1,0);
                            if(ListUtils.checkListIsNotNull(timerDanmuModelList)){
                                int beginTime = timerDanmuModelList.get(0).getBeginTime();
                                Date endTime = DateUtils.addSecondsToDate(tempDate,beginTime);
                                Date currentDate = DateUtils.getCurrentDate();
                                if(DateUtils.addMinuteToDate(endTime,30).before(currentDate)){
                                    log.info("电影开始时间不为空，当场的结束时间在0点和7点之间，发出投影关闭指令");
                                    sendCommand("projectorClose",addressId,"");
                                    projectorCacheService.setProjectCloseCount(addressId);
                                }
                                /*hour = DateUtils.getTimeHour(endTime);
                                if(hour>0 && hour<7){
                                    //关闭投影指令
                                    log.info("电影开始时间不为空，当场的结束时间在0点和7点之间，发出投影关闭指令");
                                    sendCommand("projectorClose",addressId,"");
                                    projectorCacheService.setProjectCloseCount(addressId);
                                }*/
                            }
                        }else if(movieSchedule.getStartTime()!=null){
                            Date tempDate = movieSchedule.getStartTime();
                            rpcTimerDanmuService.findByPartyIdOrderBytimeDesc(partyId,0,1);
                            List<TimerDanmuModel>  timerDanmuModelList = rpcTimerDanmuService.findByPartyIdOrderBytimeDesc(partyId,1,0);
                            if(ListUtils.checkListIsNotNull(timerDanmuModelList)){
                                int beginTime = timerDanmuModelList.get(0).getBeginTime();
                                Date endTime = DateUtils.addSecondsToDate(tempDate,beginTime);
                                Date currentDate = DateUtils.getCurrentDate();
                                if(DateUtils.addMinuteToDate(endTime,30).before(currentDate)){
                                    log.info("电影开始时间不为空，当场的结束时间在0点和7点之间，发出投影关闭指令");
                                    sendCommand("projectorClose",addressId,"");
                                    projectorCacheService.setProjectCloseCount(addressId);
                                }
                                /*hour = DateUtils.getTimeHour(endTime);
                                if(hour>0 && hour<7){
                                    //关闭投影指令
                                    log.info("电影开始时间不为空，当场的结束时间在0点和7点之间，发出投影关闭指令");
                                    sendCommand("projectorClose",addressId,"");
                                    projectorCacheService.setProjectCloseCount(addressId);
                                }*/
                            }
                        }

                    }
                }
            }
        }
    }

    @Scheduled(cron = "0/30 * * * * ?")
    private void moviePlayTimeListener(){

        log.info("电影播放时长告警");
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddressModel danmuAddress:danmuAddressList){
                String addressId = danmuAddress.getId();
                PartyLogicModel partyLogicModel = rpcPartyService.findFilmByAddressId(addressId);

                if(partyLogicModel!=null){

                    String partyId = partyLogicModel.getPartyId();
                    long movieTime = partyLogicModel.getMovieTime();

                    List<MovieScheduleModel>  movieScheduleList =  rpcMovieScheduleService.findByPartyIdAndAddressId(partyId,addressId);
                    if(ListUtils.checkListIsNotNull(movieScheduleList)){
                        MovieScheduleModel movieSchedule = movieScheduleList.get(0);
                        Date movieStartTime = movieSchedule.getMoviceStartTime();
                        Date startTime = movieSchedule.getStartTime();
                        Date currentDate = DateUtils.getCurrentDate();
                        if(movieStartTime==null){
                            long subTime = currentDate.getTime()/1000 - startTime.getTime()/1000;
                            long tempTime = 30 * 60;
                            if(subTime > tempTime){
                                log.info("发送电影开始异常告警");
                                rpcMovieAlarmService.movieStartError(partyId,addressId,0);
                            }
                        }else{
                            log.info("发送电影是否超时告警");
                            //rpcMovieAlarmService.overTime(partyId,addressId,startTime==null?0:startTime.getTime(),movieStartTime==null?0:movieStartTime.getTime());
                        }
                    }
                }
            }
        }
    }


    private void sendCommand(String command,String addressId,String callback){
        String key = ClientCommandCacheKey.PUB_ClIENT_COMMAND_CACHE + addressId;


        Map<String,Object> commandMap = new HashMap<String,Object>();

        commandMap.put("type", ProtocolConst.PROTOCOL_CLIENT_COMMAND);

        Map<String,Object> dataMap = new HashMap<String,Object>();

        dataMap.put("bcallBack",callback);
        dataMap.put("name",command);
        commandMap.put("data",dataMap);

        String message = JSON.toJSONString(commandMap);
        log.info("发送给地址:{}客户端的指令{}",addressId,message);
        redisService.set(key, message);
        redisService.expire(key, 60);

        redisService.subPub("client:command",addressId);
    }


}
