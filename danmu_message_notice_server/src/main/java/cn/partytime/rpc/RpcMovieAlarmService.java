package cn.partytime.rpc;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.cache.projector.ProjectorAlarmCacheService;
import cn.partytime.common.cachekey.client.ClientCommandCacheKey;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.*;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.model.*;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.DanmuAlarmService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private RpcMovieScheduleService rpcMovieScheduleService;


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

    /**
     * 电影超时报警
     * @param partyId
     * @param addressId
     * @param startTime 弹幕开始时间
     * @param movieStartTime  电影开始时间
     */
    @RequestMapping(value = "/overTime" ,method = RequestMethod.GET)
    public void overTime(@RequestParam String partyId,@RequestParam String addressId, @RequestParam long startTime,long movieStartTime) {
        Date currentDate = DateUtils.getCurrentDate();
        long subTime = 0;
        if(startTime!=0){
            subTime = currentDate.getTime() - startTime;
            if(subTime/60/1000<180){
                return;
            }
        }else if(movieStartTime!=0){
            subTime = currentDate.getTime() - movieStartTime;
            if(subTime/60/1000<150){
                return;
            }
        }else{
            return;
        }
        //触发时间过
        Map<String,String> map = commonDataService.setMapByAddressId(AlarmKeyConst.ALARM_KEY_MOVIEOVERTIME, addressId, partyId);
        sendMessage(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_LONG,map,AlarmKeyConst.ALARM_KEY_MOVIEOVERTIME);
    }

    /**
     * 时间太短报警
     * @param partyId
     * @param addressId
     */
    @RequestMapping(value = "/shortTime" ,method = RequestMethod.GET)
    public void shortTime(@RequestParam String partyId,@RequestParam String addressId) {
        log.info("--------------电影时长过短逻辑判断------------------------");
        List<MovieScheduleModel> movieScheduleModelList = rpcMovieScheduleService.findLastMovieListByAddressId(addressId,1,0);
        if(ListUtils.checkListIsNotNull(movieScheduleModelList)){
            MovieScheduleModel movieScheduleModel = movieScheduleModelList.get(0);
            Date startTime = movieScheduleModel.getStartTime();
            Date movieStartTime = movieScheduleModel.getMoviceStartTime();
            Date currentTime = DateUtils.getCurrentDate();
            long subTime = 0;
            if(startTime!=null){
                subTime = currentTime.getTime() - startTime.getTime();
                log.info("subTime:{}",subTime/60/1000);
                if(subTime/60/1000>60){
                    return;
                }
            }else if(movieStartTime!=null){
                subTime = currentTime.getTime() - movieStartTime.getTime();
                log.info("subTime:{}",subTime/60/1000);
                if(subTime/60/1000>60){
                    return;
                }
            }else{
                return;
            }
            Map<String,String>  map = commonDataService.setMapByAddressId(AlarmKeyConst.ALARM_KEY_MOVIESHORT, addressId, partyId);
            sendMessage(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_SHORT,map,AlarmKeyConst.ALARM_KEY_MOVIESHORT);
        }
    }
    /*@RequestMapping(value = "/movieTime" ,method = RequestMethod.GET)
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
    }*/

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
                    sendCommand("projectorClose",addressId,"");
                    /*List<DanmuClientModel> danmuClientList = rpcDanmuClientService.findByAddressId(addressId);
                    if(ListUtils.checkListIsNotNull(danmuClientList)){
                        danmuClientList.forEach(danmuClientModel -> rpcProjectorService.closeProjector(danmuClientModel.getRegistCode()));
                    }*/
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
