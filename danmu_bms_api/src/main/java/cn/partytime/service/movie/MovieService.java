package cn.partytime.service.movie;

import cn.partytime.common.cachekey.*;
import cn.partytime.common.constants.PotocolComTypeConst;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.handlerThread.PreDanmuHandler;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.MovieSchedule;
import cn.partytime.model.manager.Party;
import cn.partytime.redis.service.RedisService;
import cn.partytime.rpcService.PartyLogicService;
import cn.partytime.service.MovieScheduleService;
import cn.partytime.service.PartyService;
import com.alibaba.fastjson.JSON;
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
 * Created by Administrator on 2017/4/11 0011.
 */

@Service
public class MovieService {


    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);


    @Autowired
    private RedisService redisService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MovieScheduleService movieScheduleService;

    @Autowired
    private PreDanmuHandler preDanmuHandler;


    @Autowired
    private PartyLogicService partyLogicService;


    /*public RestResultModel movieHandler(String movieStartCommand,String command,String addressId){
        Party party = partyService.findByMovieAliasOnLine(movieStartCommand);
        RestResultModel restResultModel = new RestResultModel();
        restResultModel = checkPartyIsOk(party);
        if(restResultModel!=null){
            return restResultModel;
        }
        if(command.startsWith("danmu-start")){
            return danmuStart(party,addressId);
        }else if("movie-start".equals(command)){
            return movieStart(party,addressId);
        }else if("movie-close".equals(command)){
            return movieStop(party,addressId);
        }
        return null;
    }*/

    public RestResultModel danmuStart(String  partyId,String addressId,long clientTime){

        RestResultModel restResultModel = new RestResultModel();
        List<MovieSchedule> movieScheduleList = movieScheduleService.findByPartyIdAndAddressId(partyId, addressId);
        MovieSchedule movieSchedule = null;
        if (ListUtils.checkListIsNotNull(movieScheduleList)) {
            movieSchedule = movieScheduleList.get(0);
            Date startDate = movieSchedule.getStartTime();
            Date endDate = movieSchedule.getEndTime();
            if (startDate == null || endDate != null) {
                logger.info("最后一次插入的数据，开始时间为空，或者结束时间不为空，重新插入一条数据，证明是一个新的开始请求");
                insertmovieSchedule(partyId, addressId,clientTime);
                sendPartyStatusToClient(partyId,"1",addressId,clientTime);
                restResultModel.setResult(200);
                return restResultModel;
            } else {
                long minute = DateUtils.subMinute(startDate, DateUtils.getCurrentDate());
                if (minute < 180) {
                    logger.info("场地:{},活动:{}在180分钟内再一次内触发开始请求，本次请求忽略，活动已经开始", addressId, partyId);
                    //默认是同一个电影的开始处理
                    restResultModel.setResult(401);
                    restResultModel.setResult_msg("活动已经开始");
                    return restResultModel;
                } else {
                    logger.info("场地:{},活动:{}在180分钟外触发开始请求，插入一条新的数据", addressId, partyId);
                    //如果最后一条数据的开始时间
                    insertmovieSchedule(partyId, addressId,clientTime);
                    sendPartyStatusToClient(partyId,"1",addressId,clientTime);
                    restResultModel.setResult(200);
                    return restResultModel;
                }
            }
        } else {
            insertmovieSchedule(partyId, addressId,clientTime);
            sendPartyStatusToClient(partyId,"1",addressId,clientTime);
            restResultModel.setResult(200);
            return restResultModel;
        }
    }




    public RestResultModel movieStart(String  partyId,String addressId,long clientTime){
        RestResultModel restResultModel = new RestResultModel();
        List<MovieSchedule> movieScheduleList = movieScheduleService.findByPartyIdAndAddressId(partyId, addressId);
        MovieSchedule movieSchedule = null;
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
                        restResultModel.setResult(401);
                        restResultModel.setResult_msg("电影已经开始");
                        return restResultModel;
                    }
                }else if(startDate!=null){
                    long minute = DateUtils.subMinute(startDate, DateUtils.getCurrentDate());
                    logger.info("当前时间与上一条开始时间差是:{}",minute);
                    if (minute < 30) {
                        logger.info("电影开始的请求距离活动开始的时间是30分钟以内");
                        movieSchedule.setMoviceStartTime(DateUtils.getCurrentDate());
                        movieSchedule.setClientMoviceStartTime(clientTime);
                        movieSchedule.setUpdateTime(DateUtils.getCurrentDate());
                        movieScheduleService.updateMovieSchedule(movieSchedule);
                        sendPartyStatusToClient(partyId,"2",addressId,clientTime);
                        restResultModel.setResult(200);
                        return restResultModel;
                    }
                }
                logger.info("距离最后一条数据的活动开始时间大于30分钟，电影开始时间大于150分钟");
                insertmovieScheduleByMoviceStart(partyId, addressId,clientTime);
                sendPartyStatusToClient(partyId,"2",addressId,clientTime);
                restResultModel.setResult(200);
                return restResultModel;
            }

        } else {
            insertmovieScheduleByMoviceStart(partyId, addressId,clientTime);
            sendPartyStatusToClient(partyId,"2",addressId,clientTime);
            restResultModel.setResult(200);
            return restResultModel;
        }
    }
    public RestResultModel movieStop(String  partyId,String addressId,long clientTime){
        RestResultModel restResultModel = new RestResultModel();
        List<MovieSchedule> movieScheduleList = movieScheduleService.findByPartyIdAndAddressId(partyId, addressId);
        if (ListUtils.checkListIsNotNull(movieScheduleList)) {
            MovieSchedule movieSchedule = movieScheduleList.get(0);
            Date startDate = movieSchedule.getStartTime();
            Date movieStartDate = movieSchedule.getMoviceStartTime();
            Date endDate = movieSchedule.getEndTime();
            if (endDate != null) {
                logger.info("最后一条数据的结束时间不为空，此次请求忽略");
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

                    movieScheduleService.updateMovieSchedule(movieSchedule);

                    redisService.expire(FunctionControlCacheKey.FUNCITON_CONTROL_DANMU_DENSITY + partyId,0);
                    //清空预置弹幕
                    clearPreDanmu(addressId,partyId);
                    sendPartyStatusToClient(partyId,"3",addressId,clientTime);
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
                    movieScheduleService.updateMovieSchedule(movieSchedule);


                    redisService.expire(FunctionControlCacheKey.FUNCITON_CONTROL_DANMU_DENSITY + partyId,0);

                    //清空预置弹幕
                    clearPreDanmu(addressId,partyId);
                    sendPartyStatusToClient(partyId,"3",addressId,clientTime);
                    restResultModel.setResult(200);
                    return restResultModel;
                }
            }
            restResultModel.setResult(406);
            restResultModel.setResult_msg("活动已经结束");
            return restResultModel;
        }else{
            restResultModel.setResult(406);
            restResultModel.setResult_msg("活动已经结束");
            return restResultModel;
        }
    }

    private void clearPreDanmu(String addressId,String partyId ){
        String key = ScreenClientCacheKey.SCREEN_DANMU_COUNT+addressId;
        redisService.expire(key,0);


        key = ScreenClientCacheKey.SCREEN_DANMU_Time+addressId;
        redisService.expire(key,0);

        key = PartyCacheKey.CURRENT_PARTY+addressId;
        redisService.expire(key,0);


        String preDanmuCacheKey = PreDanmuCacheKey.PARTY_PREDANMU_CACHE_LIST + partyId;
        redisService.expire(preDanmuCacheKey,0);

    }


    private void sendPartyStatusToClient(String  partyId, String status, String addressId,long clientTime){


        Map<String,Object> commandObject = new HashMap<String,Object>();
        commandObject.put("type", ProtocolConst.PROTOCOL_COMMAND);
        Map<String,Object> dataObject = new HashMap<String,Object>();

        dataObject.put("type",PotocolComTypeConst.COMMANDTYPE_PARTY_STATUS);
        dataObject.put("partyId",partyId);
        dataObject.put("status",status);
        if("1".equals(status)){

            //判断是否有活动正在进行
            PartyLogicModel partyLogicModel = partyLogicService.findTemporaryParty(addressId);
            if(partyLogicModel!=null){
                sendCommandRestartClient(addressId);
            }
            dataObject.put("partyTime",clientTime);
            //判断这个场地有没有正在进行的活动
            /*PartyLogicModel partyLogicModel = partyLogicService.findTemporaryParty(addressId);
            if(partyLogicModel!=null){
                if(partyLogicModel.getStatus()>0 && partyLogicModel.getStatus()<3){
                    //让客户端重启
                    dataObject.put("status",1);
                }
            }*/
        }else if("2".equals(status)){
            dataObject.put("movieTime",clientTime);
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

    private void insertmovieSchedule(String partyId, String addressId,long clientTime) {
        Date date = DateUtils.getCurrentDate();
        MovieSchedule movieSchedule = new MovieSchedule();
        movieSchedule.setPartyId(partyId);
        //活动开始时间
        movieSchedule.setStartTime(date);
        movieSchedule.setAddressId(addressId);
        movieSchedule.setCreateTime(date);
        movieSchedule.setUpdateTime(date);

        movieSchedule.setClientStartTime(clientTime);

        movieScheduleService.insertMovieSchedule(movieSchedule);

        //开启预制弹幕
        logger.info("弹幕开始，开启预制弹幕");
        preDanmuHandler.danmuListenHandler(partyId);
    }

    private void insertmovieScheduleByMoviceStart(String partyId, String addressId,long clientTime) {
        MovieSchedule movieSchedule = new MovieSchedule();
        Date date = DateUtils.getCurrentDate();
        movieSchedule.setPartyId(partyId);
        //电影开始时间
        movieSchedule.setMoviceStartTime(date);
        movieSchedule.setAddressId(addressId);
        movieSchedule.setCreateTime(date);
        movieSchedule.setUpdateTime(date);

        movieSchedule.setClientMoviceStartTime(clientTime);

        movieScheduleService.insertMovieSchedule(movieSchedule);

        //开启预制弹幕
        logger.info("电影开始，开启预制弹幕");
        preDanmuHandler.danmuListenHandler(partyId);
    }

    /**
     * 插入活动时间
     * @param partyId
     * @param addressId
     */
    private void insertmovieScheduleByMoviceStop(String partyId, String addressId) {
        MovieSchedule movieSchedule = new MovieSchedule();
        Date date = DateUtils.getCurrentDate();
        movieSchedule.setPartyId(partyId);
        //结束时间
        movieSchedule.setEndTime(date);
        movieSchedule.setAddressId(addressId);
        movieSchedule.setCreateTime(date);
        movieSchedule.setUpdateTime(date);
        movieScheduleService.insertMovieSchedule(movieSchedule);
    }

    private RestResultModel checkPartyIsOk(Party party){
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
}
