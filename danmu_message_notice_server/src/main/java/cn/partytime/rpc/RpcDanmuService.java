package cn.partytime.rpc;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.constants.AlarmConst;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.dataRpc.RpcMovieScheduleService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.model.MovieScheduleModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.service.DanmuAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */
@RestController
@RequestMapping("/rpcDanmu")
@Slf4j
public class RpcDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(RpcDanmuService.class);

    @Autowired
    private DanmuAlarmService danmuAlarmService;

    @Autowired
    private MessageHandlerService messageHandlerService;

    @Autowired
    private CommonDataService commonDataService;

    @Autowired
    private AlarmCacheService alarmCacheService;

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private RpcMovieScheduleService rpcMovieScheduleService;

    @RequestMapping(value = "/danmuAlarm" ,method = RequestMethod.GET)
    public void danmuAlarm(@RequestParam String type, @RequestParam String code) {
        logger.info("告警类型:{},客户端编号:{}",type,code);
        MessageObject<Map<String,String>> mapMessageObject = null;
        Map<String,String> map = new HashMap<>();
        if(AlarmConst.DanmuAlarmType.PRE_DANMU_IS_NULL.equals(type)){
            log.info("预置弹幕没有了");
            map = commonDataService.setCommonMapByAddressId(AlarmKeyConst.ALARM_KEY_PREDANMU,code);
            sendMessage(map,LogCodeConst.DanmuLogCode.PREDANMU_ISNULL,1);

        }else if(AlarmConst.DanmuAlarmType.DANMU_IS_NULL.equals(type)){

            log.info("客户端没有弹幕了");
            map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_SYSTEMERROR,code);
            sendMessageByRule(map,LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL,1);


        }else if(AlarmConst.DanmuAlarmType.HISTORY_DANMU_IS_NULL.equals(type)){

            log.info("客户端历史弹幕没有了");
            map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_HISTORYDANMU,code);
            sendMessage(map,LogCodeConst.DanmuLogCode.CLIENT_HISTORYDANMU_ISNULL,1);

        }else if(AlarmConst.DanmuAlarmType.TIMER_DANMU_IS_NULL.equals(type)){

            log.info("客户端定时弹幕没有了");
            map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_TIMERDANMU,code);
            sendMessage(map,LogCodeConst.DanmuLogCode.CLIENT_TIMERDANMU_ISNULL,1);

        }else if(AlarmConst.DanmuAlarmType.DANMU_IS_MORE.equals(type)){

            log.info("客户端弹幕过量");
            map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_DANMUEXCESS,code);
            sendMessageByRule(map,LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISMORE,1);

        }else{
            logger.info("=============》告警类型:{},客户端编号:{}",type,code);
        }

    }

    private void sendMessageByRule(Map<String,String> map,String type,int count){
        if(map!=null){
            String addressId = map.get("addressId");
            String partyId = map.get("partyId");
            int cacheCount = alarmCacheService.findAlarmCount(addressId,type);
            if(cacheCount>= count){
                log.info("type:{}告警发出的次数超过上限",type);
                return;
            }

            long alarmOktTime = alarmCacheService.findAlarmTime(addressId,type);
            if(alarmOktTime!=0){
                long subTime = DateUtils.getCurrentDate().getTime()-alarmOktTime;
                if(subTime/1000/60<2){
                    return;
                }
            }
            long time = rpcMovieScheduleService.findByCurrentMovieLastTime(partyId,addressId);
            //告警计数
            alarmCacheService.addAlarmCount(time,addressId,type);
            //执行告警发送
            MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(type,map);
            mapMessageObject.setValue(0);
            mapMessageObject.setThreshold(0);
            messageHandlerService.messageHandler(danmuAlarmService,mapMessageObject);
        }
    }

    private void sendMessage(Map<String,String> map,String type,int count){
        if(map!=null){
            String addressId = map.get("addressId");
            String partyId = map.get("partyId");
            int cacheCount = alarmCacheService.findAlarmCount(addressId,type);
            if(cacheCount>= count){
                log.info("type:{}告警发出的次数超过上限",type);
                return;
            }
            //告警计数
            long  time = rpcMovieScheduleService.findByCurrentMovieLastTime(partyId,addressId);
            alarmCacheService.addAlarmCount(time,addressId,type);
            //执行告警发送
            MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(type,map);
            mapMessageObject.setValue(0);
            mapMessageObject.setThreshold(0);
            messageHandlerService.messageHandler(danmuAlarmService,mapMessageObject);
        }
    }
}
