package cn.partytime.scheduler;

import cn.partytime.cache.admin.CheckAdminCacheService;
import cn.partytime.cache.collector.CollectorCacheService;
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
    private CommonDataService commonDataService;

    @Autowired
    private MessageLogicService messageLogicService;

    @Autowired
    private RpcDanmuClientService rpcDanmuClientService;

    @Autowired
    private RpcProjectorService rpcProjectorService;

    @Autowired
    private RpcProjectorAlarmService rpcProjectorAlarmService;

    @Autowired
    private CheckAdminCacheService checkAdminCacheService;

    @Autowired
    private RpcAdminAlarmService rpcAdminAlarmService;

    @Autowired
    private RpcClientAlarmService rpcClientAlarmService;

    @Autowired
    private RpcMovieAlarmService rpcMovieAlarmService;

    @Value("${alarm.admin.offlineTime}")
    private int clientOfflineTime;

    @Value("${client.online.count}")
    private int clientOnlineCount;



    @Scheduled(cron = "0 5 2 * * ?")
    private void projectorCloseCommandListener(){
        log.info("投影关闭监听逻辑");
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddressModel danmuAddress:danmuAddressList){
                String addressId = danmuAddress.getId();
                log.info("场地{}:{}投影",addressId,danmuAddress.getName());
                List<DanmuClientModel>  danmuClientList = rpcDanmuClientService.findByAddressId(addressId);
                if(ListUtils.checkListIsNotNull(danmuClientList)){
                    for(DanmuClientModel danmuClient:danmuClientList){
                        String regsitrorCode = danmuClient.getRegistCode();
                        PageResultModel<ProjectorActionModel> projectorActions =  rpcProjectorService.findProjectorActionPage(regsitrorCode,0,1);
                        List<ProjectorActionModel> projectorActionList = projectorActions.getRows();
                        if(ListUtils.checkListIsNotNull(projectorActionList)){
                            ProjectorActionModel projectorAction = projectorActionList.get(0);
                            if(projectorAction.getEndTime()==null && DateUtils.checkDataIsCurrentDate(projectorAction.getCreateTime())){
                                rpcProjectorAlarmService.projectorClose(regsitrorCode);
                            }
                        }
                    }
                }
            }
        }

    }

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
                    if(count<clientOnlineCount){
                        long time = collectorCacheService.findFlashOfflineTime(addressId);
                        Date date =  DateUtils.getCurrentDate();
                        long subTime = date.getTime() - time;
                        long minute = subTime/1000/60;
                        log.info("flash 离线时间:{}",minute);
                        if(time==0){
                            //从来未启动过
                            rpcClientAlarmService.clientNetError(addressId);
                        }else if(minute>=clientOfflineTime){
                            rpcClientAlarmService.clientNetError(addressId);
                        }
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0/30 * * * * ?")
    private void moviePlayTimeListener(){
        log.info("电影播放时长监听");
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
                        if(movieSchedule.getEndTime()==null){
                            Date currentDate = DateUtils.getCurrentDate();
                            Date danmuStartTime = movieSchedule.getStartTime();
                            long subTime = currentDate.getTime() - danmuStartTime.getTime();
                            if(subTime>movieTime && movieTime!=0){
                                rpcMovieAlarmService.movieTime(partyId,addressId,subTime);
                            }
                        }
                    }
                }
            }
        }
    }


}
