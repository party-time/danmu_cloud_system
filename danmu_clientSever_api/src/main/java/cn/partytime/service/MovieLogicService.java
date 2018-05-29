package cn.partytime.service;

import cn.partytime.alarmRpc.RpcMovieAlarmService;
import cn.partytime.business.command.ControlCommandService;
import cn.partytime.cache.danmu.DanmuAlarmCacheService;
import cn.partytime.cache.party.PartyAlarmCacheService;
import cn.partytime.cache.party.PartyCacheService;
import cn.partytime.common.cachekey.CommandCacheKey;
import cn.partytime.common.cachekey.ScreenClientCacheKey;
import cn.partytime.common.cachekey.client.ClientCommandCacheKey;
import cn.partytime.common.constants.PotocolComTypeConst;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.dataRpc.RpcMovieScheduleService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.dataRpc.RpcPreDanmuService;
import cn.partytime.model.DanmuClientModel;
import cn.partytime.model.MovieScheduleModel;
import cn.partytime.model.PartyModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */

@Service
@Slf4j
public class MovieLogicService {


    private static final Logger logger = LoggerFactory.getLogger(MovieLogicService.class);

    @Autowired
    private RpcDanmuClientService rpcDanmuClientService;


    @Autowired
    private RpcMovieScheduleService rpcMovieScheduleService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RpcPartyService partyService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RpcMovieAlarmService rpcMovieAlarmService;

    @Autowired
    private PartyAlarmCacheService partyAlarmCacheService;

    @Autowired
    private PreDanmuLogicService preDanmuLogicService;

    @Autowired
    private RpcPreDanmuService rpcPreDanmuService;


    @Autowired
    private PartyCacheService partyCacheService;

    @Autowired
    private ControlCommandService controlCommandService;

    public RestResultModel partyStart(String registCode,String command,long clientTime) {
        PartyModel party = partyService.findByMovieAliasOnLine(command);
        logger.info("弹幕开始请求：指令编号：{},registCode:{}", command, registCode);
        RestResultModel restResultModel = new RestResultModel();
        DanmuClientModel danmuClient = rpcDanmuClientService.findByRegistCode(registCode);

        restResultModel = checkClientExist(danmuClient,registCode);
        if(restResultModel!=null){
            return restResultModel;
        }
        restResultModel = checkPartyIsOk(party);
        if(restResultModel!=null){
            return restResultModel;
        }

        String addressId = danmuClient.getAddressId();
        String partyId = party.getId();
        Date currentDate = DateUtils.getCurrentDate();

        //开启投影指令
        log.info("-----------------判断是否发出投影开始指令----------------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                long count = rpcMovieScheduleService.countByCreateTimeGreaterThanSeven(addressId);
                if(count==0){
                    controlCommandService.sendCommandToJavaClient("projectorStart",danmuClient.getAddressId(),"");
                }
            }
        }).start();





        MovieScheduleModel movieSchedule= rpcMovieScheduleService.findLastMovieByAddressId(addressId);
        if (movieSchedule!=null) {
            log.info("movieSchedule:{}",JSON.toJSONString(movieSchedule));
            Date startDate = movieSchedule.getStartTime();
            Date movieStartDate  = movieSchedule.getMoviceStartTime();

            String tempPartyId = movieSchedule.getPartyId();
            if(!tempPartyId.equals(partyId)){
                log.info("当前请求的电影与最后一条数据的电影不一致,结束最后一条数据的电影");
                movieSchedule.setEndTime(DateUtils.getCurrentDate());
                movieSchedule.setUpdateTime(DateUtils.getCurrentDate());
                movieSchedule.setClientEndTime(clientTime);
                rpcMovieScheduleService.updateMovieSchedule(movieSchedule);
            }else{
                log.info("startDate:{}",startDate);
                log.info("movieStartDate:{}",movieStartDate);
                log.info("currentDate:{}",movieStartDate);
                //当电影开始时间存在的时候
                if(movieStartDate!=null){
                    long minute = DateUtils.subMinute(movieStartDate,currentDate);
                    if(minute<10){
                        logger.info("当前时间距离开始时间在10分钟以内，此次请求忽略");
                        restResultModel = new RestResultModel();
                        restResultModel.setResult(201);
                        return restResultModel;
                    }else{
                        //当前时间减去电影开始时间大于10分钟
                        logger.info("当前时间减去电影开始时间大于10分钟");

                        log.info("--------------开始执行活动时间是否果过短的逻辑-----------------");
                        rpcMovieAlarmService.shortTime(partyId,addressId,movieStartDate.getTime());
                    }
                }else if(startDate!=null){
                    //当弹幕开始时间存在的时候
                    //当前时间减去弹幕开始时间大于10分钟
                    long minute = DateUtils.subMinute(startDate,currentDate);
                    if(minute<10){
                        logger.info("当前时间距离开始时间在10分钟以内，此次请求忽略");
                        restResultModel = new RestResultModel();
                        restResultModel.setResult(201);
                        return restResultModel;
                    }else{
                        logger.info("当前时间减去弹幕开始时间大于10分钟");

                        log.info("--------------开始执行活动时间是否果过短的逻辑-----------------");
                        rpcMovieAlarmService.shortTime(partyId,addressId,startDate.getTime());
                    }
                }
            }
        }
        //清除活动缓存
        log.info("--------------清理活动缓存--------------");
        clearPartyCacheInfo(addressId,partyId);

        log.info("--------------插入电影数据--------------");
        insertmovieSchedule(partyId, addressId,clientTime);

        log.info("--------------向客户端发送电影弹幕开始--------------");
        sendPartyStatusToClient(partyId,"1",addressId,clientTime);



		restResultModel = new RestResultModel();
		restResultModel.setResult(200);
        return restResultModel;
    }

    /**
     * 电影开始
     * @param partyId
     * @param registCode
     * @param clientTime
     * @return
     */
    public RestResultModel moviceStart(String partyId,String registCode,long clientTime) {
		logger.info("电影开始请求：活动编号：{},registCode:{}", partyId, registCode);
        RestResultModel restResultModel = new RestResultModel();
        DanmuClientModel danmuClient = rpcDanmuClientService.findByRegistCode(registCode);
        restResultModel = checkClientExist(danmuClient,registCode);
        if(restResultModel!=null){
            return restResultModel;
        }
        PartyModel party = partyService.getPartyByPartyId(partyId);
        restResultModel = checkPartyIsOk(party);
        if(restResultModel!=null){
            return restResultModel;
        }
		
		String addressId = danmuClient.getAddressId();
        List<MovieScheduleModel> movieScheduleList = rpcMovieScheduleService.findByPartyIdAndAddressId(partyId, addressId);
        MovieScheduleModel movieSchedule = null;
		Date currentDate = DateUtils.getCurrentDate();
        if (ListUtils.checkListIsNotNull(movieScheduleList)) {
			movieSchedule = movieScheduleList.get(0);
            Date startDate = movieSchedule.getStartTime();
            Date movieStartDate = movieSchedule.getMoviceStartTime();
            Date endDate = movieSchedule.getEndTime();
            log.info("movieStartDate:{}",movieStartDate);
            log.info("currentDate:{}",movieStartDate);
            log.info("startDate:{}",startDate);
            log.info("movieSchedule:{}",JSON.toJSONString(movieSchedule));
			if(movieStartDate!=null){
                long minute = DateUtils.subMinute(movieStartDate,currentDate);
				if(minute<10){
					logger.info("当前时间距离开始时间在10分钟以内，此次请求忽略");
					restResultModel = new RestResultModel();
					restResultModel.setResult(201);
					return restResultModel;
				}else{
                    //当前时间减去电影开始时间大于10分钟
					logger.info("当前时间减去电影开始时间大于10分钟");

                    log.info("--------------开始执行活动时间是否果过短的逻辑-----------------");
                    rpcMovieAlarmService.shortTime(partyId,addressId,movieStartDate.getTime());
                }
            }else if(startDate!=null && endDate==null){
                logger.info("电影开始的请求距离活动开始的时间是30分钟以内 || 开始时间不为空，结束时间为空");
                restResultModel = new RestResultModel();
                movieSchedule.setMoviceStartTime(DateUtils.getCurrentDate());
                movieSchedule.setClientMoviceStartTime(clientTime);
                movieSchedule.setUpdateTime(DateUtils.getCurrentDate());
                rpcMovieScheduleService.updateMovieSchedule(movieSchedule);
                sendPartyStatusToClient(partyId,"2",addressId,clientTime);
                restResultModel = new RestResultModel();
                restResultModel.setResult(200);
                return restResultModel;
            }
			
		}
        //清除活动缓存
        log.info("--------------清理活动缓存--------------");
        clearPartyCacheInfo(addressId,partyId);

        log.info("--------------插入电影数据--------------");
        insertmovieScheduleByMoviceStart(partyId, addressId,clientTime);

        log.info("--------------向客户端发送电影弹幕开始--------------");
        sendPartyStatusToClient(partyId,"1",addressId,clientTime);

        restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        return restResultModel;
    }
    /**
     * 电影结束
     * @param registCode
     * @return
     */
    public RestResultModel moviceStop(String partyId, String registCode,long clientTime) {
        logger.info("电影结束请求：活动编号：{},registCode:{}", partyId, registCode);
        RestResultModel restResultModel = new RestResultModel();
        DanmuClientModel danmuClient = rpcDanmuClientService.findByRegistCode(registCode);

        restResultModel = checkClientExist(danmuClient,registCode);
        if(restResultModel!=null){
            return restResultModel;
        }
        PartyModel party = partyService.getPartyByPartyId(partyId);
        restResultModel = checkPartyIsOk(party);
        if(restResultModel!=null){
            return restResultModel;
        }
        String addressId = danmuClient.getAddressId();

        List<MovieScheduleModel> movieScheduleList = rpcMovieScheduleService.findByPartyIdAndAddressId(partyId, addressId);
        if (ListUtils.checkListIsNotNull(movieScheduleList)) {
            MovieScheduleModel movieSchedule = movieScheduleList.get(0);
            Date startDate = movieSchedule.getStartTime();
            Date movieStartDate = movieSchedule.getMoviceStartTime();
            Date endDate = movieSchedule.getEndTime();
            if (endDate != null) {
                logger.info("最后一条数据的结束时间不为空，此次请求忽略");
                restResultModel = new RestResultModel();
                restResultModel.setResult(406);
                restResultModel.setResult_msg("活动已经结束");
                return restResultModel;
            }else if (movieStartDate != null || startDate != null) {
                movieSchedule.setEndTime(DateUtils.getCurrentDate());
                movieSchedule.setUpdateTime(DateUtils.getCurrentDate());
                movieSchedule.setClientEndTime(clientTime);
                rpcMovieScheduleService.updateMovieSchedule(movieSchedule);
                //清除活动缓存
                clearPartyCacheInfo(addressId,partyId);
            }
        }

        Map<String,Object> dataObject = new HashMap<String,Object>();
        dataObject.put("bcallBack","");
        dataObject.put("name","appRestart");
        controlCommandService.sendClientCommand(addressId,dataObject,ClientCommandCacheKey.PUB_ClIENT_COMMAND_CACHE);
        /*Map<String,Object> commandObject = new HashMap<String,Object>();
        commandObject.put("type", ProtocolConst.PROTOCOL_CLIENT_COMMAND);

        Map<String,Object> dataObject = new HashMap<String,Object>();
        dataObject.put("bcallBack","");
        dataObject.put("name","appClose");
        commandObject.put("data",dataObject);

        logger.info("下发消息给客户端:" + JSON.toJSONString(commandObject));
        String key = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_CACHE+addressId;
        String message = JSON.toJSONString(commandObject);
        logger.info("发送给服务器的客户端{}", message);
        redisService.set(key, message);
        redisService.expire(key, 60 );
        //通知客户端
        redisTemplate.convertAndSend("party:command", addressId);*/

        //结束指令
        restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        return null;
    }

    private RestResultModel checkClientExist(DanmuClientModel danmuClient, String registCode){
        if (danmuClient == null) {
            RestResultModel restResultModel = new RestResultModel();
            logger.info("注册码:{}错误", registCode);
            restResultModel.setResult(404);
            restResultModel.setResult_msg("客户端不存在!");
            return restResultModel;
        }
        return null;
    }

    private RestResultModel checkPartyIsOk(PartyModel party){
        RestResultModel restResultModel = new RestResultModel();
        if (party == null) {
            logger.info("电影不存在");
            restResultModel.setResult(404);
            restResultModel.setResult_msg("活动不存在");
            return restResultModel;
        }
        if (party.getType() == 0) {
            logger.info("不是电影场");
            restResultModel.setResult(405);
            restResultModel.setResult_msg("此活动不是电影场");
            return restResultModel;
        }

        if (party.getStatus() == 4) {
            logger.info("电影已经下线");
            restResultModel.setResult(406);
            restResultModel.setResult_msg("活动已经下线");
            return restResultModel;
        }
        return null;
    }


    private void insertmovieScheduleByMoviceStart(String partyId, String addressId,long clientTime) {
        MovieScheduleModel movieSchedule = new MovieScheduleModel();
        Date date = DateUtils.getCurrentDate();
        movieSchedule.setPartyId(partyId);
        //电影开始时间
        movieSchedule.setMoviceStartTime(date);
        movieSchedule.setAddressId(addressId);
        movieSchedule.setCreateTime(date);
        movieSchedule.setUpdateTime(date);
        movieSchedule.setClientMoviceStartTime(clientTime);
        rpcMovieScheduleService.insertMovieSchedule(movieSchedule);

        //开启预制弹幕
        logger.info("电影开始，启动一个线程开启预置弹幕");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    preDanmuLogicService.danmuListenHandler(partyId,addressId);
                }catch (Exception e){
                    logger.info("开启预置弹幕异常");
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void insertmovieSchedule(String partyId, String addressId,long clientTime) {
        Date date = DateUtils.getCurrentDate();
        MovieScheduleModel movieSchedule = new MovieScheduleModel();
        movieSchedule.setPartyId(partyId);
        //活动开始时间
        movieSchedule.setStartTime(date);
        movieSchedule.setAddressId(addressId);
        movieSchedule.setCreateTime(date);
        movieSchedule.setUpdateTime(date);
        movieSchedule.setClientStartTime(clientTime);
        rpcMovieScheduleService.insertMovieSchedule(movieSchedule);

        //开启预制弹幕
        logger.info("弹幕开始，启动一个线程开启预置弹幕");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    preDanmuLogicService.danmuListenHandler(partyId,addressId);
                }catch (Exception e){
                    logger.info("开启预置弹幕异常");
                    e.printStackTrace();
                }

            }
        }).start();

    }


    private void sendPartyStatusToClient(String  partyId, String status, String addressId,long clientTime){
        Map<String,Object> commandObject = new HashMap<String,Object>();
        commandObject.put("type", ProtocolConst.PROTOCOL_COMMAND);
        Map<String,Object> dataObject = new HashMap<String,Object>();

        dataObject.put("type", PotocolComTypeConst.COMMANDTYPE_PARTY_STATUS);
        dataObject.put("partyId",partyId);
        dataObject.put("status",status);
        if("1".equals(status)){
            //清除告警缓存
            partyAlarmCacheService.removeAlarmAllCache(addressId,1);
            sendCommandRestartClient(addressId);
            dataObject.put("partyTime",clientTime);
        }else if("2".equals(status)){
            dataObject.put("movieTime",clientTime);
        }else if("3".equals(status)){
            //清除告警缓存
            partyAlarmCacheService.removeAlarmAllCache(addressId,1);
        }
        commandObject.put("data",dataObject);

        logger.info("下发消息给客户端:" + JSON.toJSONString(commandObject));
        String key = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_CACHE+addressId;
        String message = JSON.toJSONString(commandObject);
        logger.info("发送给服务器的客户端{}", message);
        redisService.set(key, message);
        redisService.expire(key, 60 );
        //通知客户端
        redisTemplate.convertAndSend("party:command", addressId);
    }

    private void sendCommandRestartClient(String addressId){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("bcallBack",null);
        dataMap.put("name","appRestart");
        controlCommandService.sendClientCommand(addressId,dataMap,ClientCommandCacheKey.PUB_ClIENT_COMMAND_CACHE);
    }

    private void clearPartyCacheInfo(String addressId,String partyId ){
        String key = ScreenClientCacheKey.SCREEN_DANMU_COUNT+addressId;
        redisService.expire(key,0);
        key = ScreenClientCacheKey.SCREEN_DANMU_Time+addressId;
        redisService.expire(key,0);
        partyCacheService.removeCurrentParty(addressId);
    }

}
