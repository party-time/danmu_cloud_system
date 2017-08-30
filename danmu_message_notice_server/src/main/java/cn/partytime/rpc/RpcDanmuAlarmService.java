package cn.partytime.rpc;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.cache.danmu.DanmuAlarmCacheService;
import cn.partytime.common.cachekey.DanmuCacheKey;
import cn.partytime.common.constants.AlarmConst;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcDanmuService;
import cn.partytime.dataRpc.RpcMovieScheduleService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.dataRpc.RpcTimerDanmuService;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.service.DanmuAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */
@RestController
@RequestMapping("/rpcDanmu")
@Slf4j
public class RpcDanmuAlarmService {

    private static final Logger logger = LoggerFactory.getLogger(RpcDanmuAlarmService.class);

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


    @Autowired
    private DanmuAlarmCacheService danmuAlarmCacheService;

    @Autowired
    private RpcTimerDanmuService rpcTimerDanmuService;

    @Autowired
    private RpcDanmuService rpcDanmuService;

    @RequestMapping(value = "/danmuAlarm" ,method = RequestMethod.GET)
    public void danmuAlarm(@RequestParam String type, @RequestParam String code,@RequestParam String idd) {

        MessageObject<Map<String,String>> mapMessageObject = null;
        Map<String,String> map = new HashMap<>();
        if(AlarmConst.DanmuAlarmType.PRE_DANMU_IS_NULL.equals(type)){
            logger.info("告警类型:{},场地编号:{}",type,code);
            log.info("预置弹幕没有了");
            int cacheCount = alarmCacheService.findAlarmCount(code,AlarmKeyConst.ALARM_KEY_PREDANMU);
            if(cacheCount>= 1){
                log.info("type:{}告警发出的次数超过上限",type);
                return;
            }
            map = commonDataService.setCommonMapByAddressId(AlarmKeyConst.ALARM_KEY_PREDANMU,code);
            sendMessage(map,LogCodeConst.DanmuLogCode.PREDANMU_ISNULL,1,AlarmKeyConst.ALARM_KEY_PREDANMU);

        }else if(AlarmConst.DanmuAlarmType.DANMU_IS_NULL.equals(type)){

            logger.info("告警类型:{},客户端编号:{}",type,code);
            log.info("客户端没有弹幕了");
            map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_SYSTEMERROR,code);
            sendMessageByRule(map,LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL,1,AlarmKeyConst.ALARM_KEY_SYSTEMERROR);


        }else if(AlarmConst.DanmuAlarmType.HISTORY_DANMU_IS_NULL.equals(type)){

            logger.info("告警类型:{},客户端编号:{}",type,code);
            log.info("客户端历史弹幕没有了");
            map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_HISTORYDANMU,code);
            sendMessage(map,LogCodeConst.DanmuLogCode.CLIENT_HISTORYDANMU_ISNULL,1,AlarmKeyConst.ALARM_KEY_HISTORYDANMU);

        }else if(AlarmConst.DanmuAlarmType.TIMER_DANMU_IS_NULL.equals(type)){

            if("null".equals(idd)){
                logger.info("告警类型:{},客户端编号:{}",type,code);
                log.info("客户端定时弹幕没有了");
                map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_TIMERDANMU,code);
                sendMessage(map,LogCodeConst.DanmuLogCode.CLIENT_TIMERDANMU_ISNULL,1,AlarmKeyConst.ALARM_KEY_TIMERDANMU);
            }else{
                logger.info("告警类型:{},客户端编号:{},资源编号:{}",type,code,idd);
                log.info("客户端定时弹幕没有了");
                map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.RESOURCENOTPLAY,code);
                sendMessageByRule(map,LogCodeConst.DanmuLogCode.CLIENT_TIMERDANMU_NOT_PLAY,idd,1,AlarmKeyConst.RESOURCENOTPLAY);
            }


        }else if(AlarmConst.DanmuAlarmType.DANMU_IS_MORE.equals(type)){

            logger.info("告警类型:{},客户端编号:{}",type,code);
            log.info("客户端弹幕过量");
            map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_DANMUEXCESS,code);
            sendMessageByRule(map,LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISMORE,1,AlarmKeyConst.ALARM_KEY_DANMUEXCESS);

        }else{
            logger.info("=============》告警类型:{},客户端编号:{}",type,code);
        }

    }


    private void sendMessageByRule(Map<String,String> map,String type,String idd, int count,String typeName){
        if(map!=null){
            String addressId = map.get("addressId");
            String partyId = map.get("partyId");
            long time = rpcMovieScheduleService.findByCurrentMovieLastTime(partyId,addressId);
            boolean isExist = danmuAlarmCacheService.timerDanmuNotPlayResourceisExist(addressId,idd);
            if(isExist){
                log.info("场地:{},编号:{}资源告警已经发送过",addressId,idd);
                return;
            }
            danmuAlarmCacheService.addTimerDanmuNotPlayResource(addressId,idd,time);
            //执行告警发送
            MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(type,map);
            mapMessageObject.setValue(0);
            mapMessageObject.setThreshold(0);
            messageHandlerService.messageHandler(danmuAlarmService,mapMessageObject);
        }

    }
    private void sendMessageByRule(Map<String,String> map,String type,int count,String typeName){
        if(map!=null){
            String addressId = map.get("addressId");
            String partyId = map.get("partyId");
            int cacheCount = alarmCacheService.findAlarmCount(addressId,typeName);
            if(cacheCount>= count){
                log.info("{}告警发出的次数超过上限",typeName);
                return;
            }

            long alarmOktTime = alarmCacheService.findAlarmTime(addressId,typeName);
            if(alarmOktTime!=0){
                long subTime = DateUtils.getCurrentDate().getTime()-alarmOktTime;
                long minute = subTime/1000/60;
                log.info("time:{}",minute);
                if(minute<2){
                    return;
                }
            }
            long time = rpcMovieScheduleService.findByCurrentMovieLastTime(partyId,addressId);
            log.info("计数缓存时长:{}",time);
            //告警计数
            alarmCacheService.addAlarmCount(time,addressId,typeName);
            //执行告警发送
            MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(type,map);
            mapMessageObject.setValue(0);
            mapMessageObject.setThreshold(0);
            messageHandlerService.messageHandler(danmuAlarmService,mapMessageObject);
        }
    }

    private void sendMessage(Map<String,String> map,String type,int count,String typeName){
        if(map!=null){
            String addressId = map.get("addressId");
            String partyId = map.get("partyId");
            int cacheCount = alarmCacheService.findAlarmCount(addressId,typeName);
            if(cacheCount>= count){
                log.info("{}:告警发出的次数超过上限",typeName);
                return;
            }
            long  time = rpcMovieScheduleService.findByCurrentMovieLastTime(partyId,addressId);
            log.info("当前活动进行的时间:{}",time);

            log.info("==========:{}",typeName);
            if(AlarmKeyConst.ALARM_KEY_HISTORYDANMU.equals(typeName)){
                if(time==0){
                    return;
                }
                List<String> danmuPoolIdList = rpcDanmuService.findDanmuPoolIdListByPartyIdAndAddressId(partyId,addressId);
                log.info("弹幕池编号:{}");
                if(ListUtils.checkListIsNotNull(danmuPoolIdList)){
                   long danmuCount = rpcDanmuService.countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlgAndTimeLessThan(danmuPoolIdList,1,false,time);
                   if(danmuCount<1){
                      return;
                   }
                }

            }else if(AlarmKeyConst.ALARM_KEY_TIMERDANMU.equals(typeName)){
                if(time==0){
                    //电影结束了
                    return;
                }
                boolean isExist =  rpcTimerDanmuService.findTimerDanmuIsExistAfterCurrentTime(partyId,DateUtils.getCurrentDate().getTime());
                log.info("没有定时弹幕:{}",isExist);
                if(!isExist){
                    log.info("没有定时弹幕了");
                    return;
                }
                alarmCacheService.addAlarmCount(time,addressId,typeName);
            }else{
                alarmCacheService.addAlarmCount(0,addressId,typeName);
            }

            //执行告警发送
            MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(type,map);
            mapMessageObject.setValue(0);
            mapMessageObject.setThreshold(0);
            messageHandlerService.messageHandler(danmuAlarmService,mapMessageObject);
        }
    }
}
