package cn.partytime.service;

import cn.partytime.alarmRpc.RpcMovieAlarmService;
import cn.partytime.alarmRpc.RpcProjectorAlarmService;
import cn.partytime.business.command.ControlCommandService;
import cn.partytime.cache.danmu.DanmuAlarmCacheService;
import cn.partytime.cache.party.PartyAlarmCacheService;
import cn.partytime.cache.party.PartyCacheService;
import cn.partytime.common.cachekey.*;
import cn.partytime.common.cachekey.client.ClientCommandCacheKey;
import cn.partytime.common.cachekey.danmu.PreDanmuCacheKey;
import cn.partytime.common.cachekey.party.PartyCacheKey;
import cn.partytime.common.constants.PotocolComTypeConst;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.*;
import cn.partytime.model.*;
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
    private RpcProjectorService rpcProjectorService;

    @Autowired
    private RpcDanmuClientService rpcDanmuClientService;

    @Autowired
    private RpcProjectorAlarmService rpcProjectorAlarmService;

    @Autowired
    private RpcMovieScheduleService rpcMovieScheduleService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RpcPartyService partyService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DanmuAlarmCacheService danmuAlarmCacheService;

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

        //开启投影指令

        long count = rpcMovieScheduleService.countByCreateTimeGreaterThanSeven();
        if(count==0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //logger.info("0======================");
                    controlCommandService.sendCommandToJavaClient("projectorStart",danmuClient.getAddressId(),"");
                }
            }).start();
        }

        String addressId = danmuClient.getAddressId();
        String partyId = party.getId();
        Date currentDate = DateUtils.getCurrentDate();
        List<MovieScheduleModel> movieScheduleList = rpcMovieScheduleService.findByPartyIdAndAddressId(partyId, addressId);
        MovieScheduleModel movieSchedule = null;
        if (ListUtils.checkListIsNotNull(movieScheduleList)) {
            movieSchedule = movieScheduleList.get(0);
            Date startDate = movieSchedule.getStartTime();
            Date movieStartDate  = movieSchedule.getMoviceStartTime();

            rpcMovieAlarmService.shortTime(partyId,addressId);
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
                }
            }
        }
        //清除活动缓存
        clearPartyCacheInfo(addressId,partyId);

		insertmovieSchedule(partyId, addressId,clientTime);
		firstDanmuStartCommandHandler(registCode);
		sendPartyStatusToClient(partyId,"1",addressId,clientTime);
		restResultModel = new RestResultModel();
		restResultModel.setResult(200);
        return restResultModel;
    }
    /*public RestResultModel partyStart(String registCode,String command,long clientTime) {
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
        List<MovieScheduleModel> movieScheduleList = rpcMovieScheduleService.findByPartyIdAndAddressId(partyId, addressId);
        MovieScheduleModel movieSchedule = null;
        if (ListUtils.checkListIsNotNull(movieScheduleList)) {
            movieSchedule = movieScheduleList.get(0);
            Date startDate = movieSchedule.getStartTime();
            Date endDate = movieSchedule.getEndTime();
            if (startDate == null || endDate != null) {
                logger.info("最后一次插入的数据，开始时间为空，或者结束时间不为空，重新插入一条数据，证明是一个新的开始请求");
                insertmovieSchedule(partyId, addressId,clientTime);
                firstDanmuStartCommandHandler(registCode);
                sendPartyStatusToClient(partyId,"1",addressId,clientTime);
                restResultModel = new RestResultModel();
                restResultModel.setResult(200);
                return restResultModel;
            } else {
                long minute = DateUtils.subMinute(startDate, DateUtils.getCurrentDate());
                if (minute < 180) {
                    logger.info("场地:{},活动:{}在180分钟内再一次内触发开始请求，本次请求忽略，活动已经开始", addressId, partyId);
                    //默认是同一个电影的开始处理
                    restResultModel = new RestResultModel();
                    restResultModel.setResult(401);
                    restResultModel.setResult_msg("活动已经开始");
                    return restResultModel;
                } else {
                    logger.info("场地:{},活动:{}在180分钟外触发开始请求，插入一条新的数据", addressId, partyId);
                    //如果最后一条数据的开始时间
                    restResultModel = new RestResultModel();
                    insertmovieSchedule(partyId, addressId,clientTime);
                    firstDanmuStartCommandHandler(registCode);
                    sendPartyStatusToClient(partyId,"1",addressId,clientTime);
                    restResultModel = new RestResultModel();
                    restResultModel.setResult(200);
                    return restResultModel;
                }
            }
        } else {
            insertmovieSchedule(partyId, addressId,clientTime);
            sendPartyStatusToClient(partyId,"1",addressId,clientTime);
            restResultModel = new RestResultModel();
            restResultModel.setResult(200);
            return restResultModel;
        }
    }*/

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
                }
            }else if(startDate!=null){
                logger.info("电影开始的请求距离活动开始的时间是30分钟以内");
                restResultModel = new RestResultModel();
                movieSchedule.setMoviceStartTime(DateUtils.getCurrentDate());
                movieSchedule.setClientMoviceStartTime(clientTime);
                movieSchedule.setUpdateTime(DateUtils.getCurrentDate());
                rpcMovieScheduleService.updateMovieSchedule(movieSchedule);
                sendPartyStatusToClient(partyId,"2",addressId,clientTime);
                restResultModel = new RestResultModel();
                restResultModel.setResult(200);
                return restResultModel;
            }/*else if(startDate!=null){
                //当弹幕开始时间存在的时候
				//当前时间减去弹幕开始时间大于10分钟
                long minute = DateUtils.subMinute(startDate,currentDate);
				if(minute<10 + x){
					logger.info("当前时间距离开始时间在10分钟以内，此次请求忽略");
					restResultModel = new RestResultModel();
					restResultModel.setResult(201);
					return restResultModel;
				}else{
                    logger.info("当前时间减去弹幕开始时间大于10分钟");
                }
            }*/
			
		}
        //清除活动缓存
        clearPartyCacheInfo(addressId,partyId);
        insertmovieScheduleByMoviceStart(partyId, addressId,clientTime);
        sendPartyStatusToClient(partyId,"3",addressId,clientTime);
        restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        return restResultModel;
    }
    /*public RestResultModel moviceStart(String partyId,String registCode,long clientTime) {
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
        if (ListUtils.checkListIsNotNull(movieScheduleList)) {
            movieSchedule = movieScheduleList.get(0);
            Date startDate = movieSchedule.getStartTime();
            Date movieStartDate = movieSchedule.getMoviceStartTime();
            Date endDate = movieSchedule.getEndTime();
            //如果最后一条数据的开始时间是空，直接插入数据
            if (endDate != null) {
                logger.info("最后一次插入的数据，结束时间为空，一个新的电影开始请求");
                logger.info("场地:{},活动:{} 请求活动开始");
                insertmovieScheduleByMoviceStart(partyId, addressId,clientTime);
                sendPartyStatusToClient(partyId,"2",addressId,clientTime);
                restResultModel = new RestResultModel();
                restResultModel.setResult(200);
                return restResultModel;
            } else {

                if (movieStartDate != null) {
                    long minute = DateUtils.subMinute(movieStartDate, DateUtils.getCurrentDate());
                    //最后一条数据的电影开始时间不为空。
                    //判断这次请求的时间和最后一条数据的电影开始时间对比
                    if (minute < 150) {
                        logger.info("本次请求电影开始，与上次请求的电影开始时间差在150分钟以内，认为是重复相同的请求，此次忽略不处理");
                        //默认是同一个电影的开始处理
                        restResultModel = new RestResultModel();
                        restResultModel.setResult(401);
                        restResultModel.setResult_msg("电影已经开始");
                        return restResultModel;
                    }
                }else if(startDate!=null){
                    long minute = DateUtils.subMinute(startDate, DateUtils.getCurrentDate());
                    logger.info("当前时间与上一条开始时间差是:{}",minute);
                    if (minute < 30) {
                        logger.info("电影开始的请求距离活动开始的时间是30分钟以内");
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
                logger.info("距离最后一条数据的活动开始时间大于30分钟，电影开始时间大于150分钟");
                insertmovieScheduleByMoviceStart(partyId, addressId,clientTime);
                sendPartyStatusToClient(partyId,"2",addressId,clientTime);
                restResultModel = new RestResultModel();
                restResultModel.setResult(200);
                return restResultModel;
            }

        } else {
            insertmovieScheduleByMoviceStart(partyId, addressId,clientTime);
            sendPartyStatusToClient(partyId,"2",addressId,clientTime);
            restResultModel = new RestResultModel();
            restResultModel.setResult(200);
            return restResultModel;
        }
    }*/

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
            }else if (movieStartDate != null) {
                long minutemoviestart = DateUtils.subMinute(movieStartDate, DateUtils.getCurrentDate());
                logger.info("当前时间距离最一条数据的电影开始时间是150分钟以内可以触发结束");
                movieSchedule.setEndTime(DateUtils.getCurrentDate());
                movieSchedule.setUpdateTime(DateUtils.getCurrentDate());
                movieSchedule.setClientEndTime(clientTime);
                rpcMovieScheduleService.updateMovieSchedule(movieSchedule);
                //清除活动缓存
                clearPartyCacheInfo(addressId,partyId);
            }else if (startDate != null) {
                logger.info("当前时间距离最一条数据的电影开始时间是150分钟以内可以触发结束");
                movieSchedule.setEndTime(DateUtils.getCurrentDate());
                movieSchedule.setUpdateTime(DateUtils.getCurrentDate());
                movieSchedule.setClientEndTime(clientTime);
                rpcMovieScheduleService.updateMovieSchedule(movieSchedule);
                //清除活动缓存
                clearPartyCacheInfo(addressId,partyId);
            }
        }

        Map<String,Object> commandObject = new HashMap<String,Object>();
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
        redisTemplate.convertAndSend("party:command", addressId);

        //结束指令
        restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        return null;
    }
    /*public RestResultModel moviceStop(String partyId, String registCode,long clientTime) {
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

        restResultModel = new RestResultModel();
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
            }else if (movieStartDate != null) {
                long minutemoviestart = DateUtils.subMinute(movieStartDate, DateUtils.getCurrentDate());
                if (minutemoviestart < 150) {
                    logger.info("当前时间距离最一条数据的电影开始时间是150分钟以内可以触发结束");
                    movieSchedule.setEndTime(DateUtils.getCurrentDate());
                    movieSchedule.setUpdateTime(DateUtils.getCurrentDate());
                    movieSchedule.setClientEndTime(clientTime);

                    //清除活动缓存
                    clearPartyCacheInfo(addressId,partyId);

                    //stop movie
                    stopMovie(movieSchedule,party);

                    sendPartyStatusToClient(partyId,"3",addressId,clientTime);
                    restResultModel = new RestResultModel();
                    restResultModel.setResult(200);
                    return restResultModel;
                }
            }else if (startDate != null) {
                long minute = DateUtils.subMinute(startDate, DateUtils.getCurrentDate());
                if (minute < 180) {
                    logger.info("当前时间距离最一条数据的开始时间是180分钟以内可以触发结束");
                    movieSchedule.setEndTime(DateUtils.getCurrentDate());
                    movieSchedule.setUpdateTime(DateUtils.getCurrentDate());

                    movieSchedule.setClientEndTime(clientTime);

                    //清空活动缓存信息
                    clearPartyCacheInfo(addressId,partyId);

                    stopMovie(movieSchedule,party);

                    sendPartyStatusToClient(partyId,"3",addressId,clientTime);
                    restResultModel = new RestResultModel();
                    restResultModel.setResult(200);
                    return restResultModel;
                }
            }
            restResultModel = new RestResultModel();
            restResultModel.setResult(406);
            restResultModel.setResult_msg("活动已经结束");
            return restResultModel;
        }else{
            restResultModel = new RestResultModel();
            restResultModel.setResult(406);
            restResultModel.setResult_msg("活动已经结束");
            return restResultModel;
        }
    }*/




    public void firstDanmuStartCommandHandler(String registCode){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DanmuClientModel danmuClient =  rpcDanmuClientService.findByRegistCode(registCode);
                String addressId = danmuClient.getAddressId();
                List<DanmuClientModel> danmuClientList = rpcDanmuClientService.findByAddressId(addressId);
                if(ListUtils.checkListIsNotNull(danmuClientList)){
                    for(DanmuClientModel tempDanmuClient:danmuClientList){
                        String code = tempDanmuClient.getRegistCode();
                        PageResultModel<ProjectorActionModel> projectorActions =  rpcProjectorService.findProjectorActionPage(code,0,1);
                        List<ProjectorActionModel> projectorActionList =projectorActions.getRows();
                        if(ListUtils.checkListIsNotNull(projectorActionList)){
                            for(ProjectorActionModel projectorAction :projectorActionList){
                                if(!DateUtils.checkDataIsCurrentDate(projectorAction.getCreateTime())){
                                    rpcProjectorAlarmService.projectorOpen(code);
                                }
                                //如果最后一条数据的结束时间不为空
                                if(projectorAction.getEndTime()!=null){
                                    rpcProjectorAlarmService.projectorOpen(code);
                                }
                            }
                        }else{
                            rpcProjectorAlarmService.projectorOpen(code);
                        }
                    }
                }
            }
        }).start();
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
        logger.info("电影开始，开启预制弹幕");
        preDanmuLogicService.danmuListenHandler(partyId,addressId);
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
        logger.info("弹幕开始，开启预制弹幕");
        preDanmuLogicService.danmuListenHandler(partyId,addressId);

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

            //判断是否有活动正在进行
            //PartyLogicModel partyLogicModel = partyService.findTemporaryParty(addressId);
            //if(partyLogicModel!=null){
                sendCommandRestartClient(addressId);
            //}
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
        String key = ClientCommandCacheKey.PUB_ClIENT_COMMAND_CACHE + addressId;
        Map<String,Object> commandMap = new HashMap<>();

        commandMap.put("type", ProtocolConst.PROTOCOL_CLIENT_COMMAND);

        Map<String,Object> dataMap = new HashMap<>();

        dataMap.put("bcallBack",null);
        dataMap.put("name","appRestart");
        commandMap.put("data",dataMap);


        redisService.set(key, JSON.toJSONString(commandMap));
        redisService.expire(key, 60);
        redisTemplate.convertAndSend("client:command", addressId);
    }

    private void stopMovie(MovieScheduleModel movieSchedule,PartyModel party){
        movieSchedule = rpcMovieScheduleService.updateMovieSchedule(movieSchedule);
        long time = 0;
        if(party!=null && party.getMovieTime()==0 && movieSchedule.getMoviceStartTime()!=null){
            time = movieSchedule.getEndTime().getTime()-movieSchedule.getMoviceStartTime().getTime();
            party.setMovieTime(time);
            party = partyService.saveParty(party);
        }

        String partyId = party.getId();
        String addressId = movieSchedule.getAddressId();

        //加载预置弹幕
        new Thread(new Runnable() {
            @Override
            public void run() {
                rpcPreDanmuService.reInitPreDanmuIntoCache(partyId,addressId);
            }
        }).start();

        if(movieSchedule.getMoviceStartTime()==null){
            log.info("电影未正常开始，告警请求");
            rpcMovieAlarmService.movieStartError(partyId,addressId,time);
        }else{
            log.info("电影告警请求");
            time = movieSchedule.getEndTime().getTime()-movieSchedule.getMoviceStartTime().getTime();
            //rpcMovieAlarmService.movieTime(partyId,addressId,time);
        }
    }

    private void clearPartyCacheInfo(String addressId,String partyId ){
        String key = ScreenClientCacheKey.SCREEN_DANMU_COUNT+addressId;
        redisService.expire(key,0);
        key = ScreenClientCacheKey.SCREEN_DANMU_Time+addressId;
        redisService.expire(key,0);
        partyCacheService.removeCurrentParty(addressId);


        //rpcPreDanmuService.removePreDanmuCache(partyId,addressId);

    }

}
