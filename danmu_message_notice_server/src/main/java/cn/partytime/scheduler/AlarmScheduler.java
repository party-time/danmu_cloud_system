package cn.partytime.scheduler;

import cn.partytime.common.cachekey.ClientCacheKey;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.*;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.logicService.FlashClientLogicService;
import cn.partytime.logicService.MessageLogicService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.model.*;
import cn.partytime.redis.service.RedisService;
import cn.partytime.rpc.RpcProjectorAlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@EnableScheduling
@RefreshScope
public class AlarmScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AlarmScheduler.class);


    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private FlashClientLogicService flashClientLogicService;

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

    @Scheduled(cron = "0 5 2 * * ?")
    private void projectorCloseCommandListener(){
        logger.info("投影关闭监听逻辑");
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddressModel danmuAddress:danmuAddressList){
                String addressId = danmuAddress.getId();
                logger.info("场地{}:{}投影",addressId,danmuAddress.getName());
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

    @Scheduled(cron = "0 0/10 * * * *")
    public void clientOffLineScheduler() {
        logger.info("客户端在线数量监听");
        List<DanmuAddressModel> danmuAddressModels = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressModels)) {
            for (DanmuAddressModel danmuAddressModel : danmuAddressModels) {
                PartyLogicModel partyLogicModel = rpcPartyService.findFilmByAddressId(danmuAddressModel.getId());
                if(partyLogicModel!=null){
                    String partyId = partyLogicModel.getPartyId();
                    String addressId = partyLogicModel.getAddressId();
                    int count  = flashClientLogicService.findClientFlashCount(addressId);
                    if(count<2){
                        Object object = redisService.get(ClientCacheKey.ClIENT_OFFLINE_TIME+addressId);
                        if(object!=null){
                            long time = Long.parseLong(String.valueOf(object));
                            sendAlaram(time,addressId);
                        }else{
                            //管理员从来未登陆过
                            MovieScheduleModel movieScheduleModel = rpcMovieScheduleService.findCurrentMovie(partyId,addressId);
                            if(movieScheduleModel.getStartTime()!=null){
                                Date movieStartTime = movieScheduleModel.getStartTime();
                                sendAlaram(movieStartTime.getTime(),addressId);
                            }
                        }
                    }
                }
            }
        }
    }
    public void sendAlaram(long  time,String addressId){
        Date date =  DateUtils.getCurrentDate();
        int alarmCount = flashClientLogicService.findClientAlarmCount(addressId);
        if(alarmCount>0){
            logger.info("告警已经发出");
            return;
        }
        long subTime = date.getTime() - time;
        long minute = subTime/1000/60;
        if(minute>5){
            Map<String,String> map = commonDataService.setCommonMapByAddressId(AlarmKeyConst.ALARM_KEY_NETWORKERROR,addressId);
            MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.CLientLogCode.FLASH_NETWORK_EXCEPTION,map);
            messageLogicService.sendMessage(mapMessageObject);
            flashClientLogicService.addClientAlarmCount(1,addressId);
        }
    }


}
