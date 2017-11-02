package cn.partytime.rpc;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.cachekey.client.ClientCommandCacheKey;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.model.PartyModel;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.DanmuAlarmService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */
@RestController
@RequestMapping("/rpcMovie")
@Slf4j
public class RpcMovieAlarmService {
    @Autowired
    private DanmuAlarmService danmuAlarmService;

    @Autowired
    private MessageHandlerService messageHandlerService;

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private CommonDataService commonDataService;

    @Autowired
    private AlarmCacheService alarmCacheService;

    @Autowired
    private RedisService redisService;


    @RequestMapping(value = "/movieStartError" ,method = RequestMethod.GET)
    public void movieStartError(@RequestParam String partyId,@RequestParam String addressId, @RequestParam long time) {
        log.info("==================================movieStartError");
        PartyModel party = rpcPartyService.getPartyByPartyId(partyId);
        Map<String,String> map = new HashMap<String,String>();
        if(time==0){
            log.info("当time为0的时候，电影没有正常开始");
            map = commonDataService.setMapByAddressId(AlarmKeyConst.ALARM_MOVIESTARTERROR,addressId,partyId);
            sendMessage(LogCodeConst.PartyLogCode.MOVIE_START_ERROR,map,AlarmKeyConst.ALARM_MOVIESTARTERROR);
        }
    }

    @RequestMapping(value = "/movieTime" ,method = RequestMethod.GET)
    public void movieTime(@RequestParam String partyId,@RequestParam String addressId, @RequestParam long time) {

        PartyModel party = rpcPartyService.getPartyByPartyId(partyId);

        MessageObject<Map<String,String>> mapMessageObject = null;
        Map<String,String> map = new HashMap<String,String>();
        if(party!=null && party.getMovieTime()!=0){
            long movieTime = party.getMovieTime();

            long timeMinute = time/1000/60;
            long movieMinute = party.getMovieTime()/1000/60;
            log.info("time:{},moviTime:{}",timeMinute,movieMinute);
            if(timeMinute < movieMinute){
                //触发事件过短
                map = commonDataService.setMapByAddressId(AlarmKeyConst.ALARM_KEY_MOVIESHORT,addressId,partyId);
                //mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_SHORT,map);
                sendMessage(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_SHORT,map,AlarmKeyConst.ALARM_KEY_MOVIESHORT);
            }else if(timeMinute > movieMinute) {
                //触发时间过
                map = commonDataService.setMapByAddressId(AlarmKeyConst.ALARM_KEY_MOVIEOVERTIME,addressId,partyId);
                //mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_SHORT,map);
                sendMessage(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_LONG,map,AlarmKeyConst.ALARM_KEY_MOVIEOVERTIME);
            }else{
                return;
            }
        }else {
            long minute = time / 1000 / 60;
            log.info("minute:{}", minute);
            if (minute < 90) {
                //触发事件过短
                map = commonDataService.setMapByAddressId(AlarmKeyConst.ALARM_KEY_MOVIESHORT, addressId, partyId);
                sendMessage(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_SHORT,map,AlarmKeyConst.ALARM_KEY_MOVIESHORT);
            } else if (minute > 180) {
                //触发时间过
                map = commonDataService.setMapByAddressId(AlarmKeyConst.ALARM_KEY_MOVIEOVERTIME, addressId, partyId);
                sendMessage(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_LONG,map,AlarmKeyConst.ALARM_KEY_MOVIEOVERTIME);

            }
        }
    }

    private void sendMessage(String type,Map<String,String> map,String typeName){
        if(map!=null){
            String addressId = map.get("addressId");
            String partyId = map.get("partyId");
            int cacheCount = alarmCacheService.findAlarmCount(addressId,typeName);
            if(cacheCount>0){
                log.info("{}:告警发出的次数超过上限",typeName);
                return;
            }

            if(AlarmKeyConst.ALARM_KEY_MOVIEOVERTIME.equals(typeName)){
                log.info("电影超时告警发出后判断当前时间是否是零点以后 三点之前发出的报警");
                int hour = DateUtils.getCurrentHour();
                log.info("当前时间是:{}",hour);
                if(hour>=0 && hour<=3 ){
                    sendCommand("projectClose",addressId,"");
                }
            }

            alarmCacheService.addAlarmCount(0,addressId,typeName);
            MessageObject<Map<String,String>>  mapMessageObject = new MessageObject<Map<String,String>>(type,map);
            mapMessageObject.setValue(0);
            mapMessageObject.setThreshold(0);
            messageHandlerService.messageHandler(danmuAlarmService,mapMessageObject);
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
