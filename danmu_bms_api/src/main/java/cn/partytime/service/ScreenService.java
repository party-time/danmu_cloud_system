package cn.partytime.service;

import cn.partytime.common.cachekey.CommandCacheKey;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.common.util.PartyTypeEnmu;
import cn.partytime.handlerThread.PreDanmuHandler;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.client.DanmuClient;
import cn.partytime.model.manager.Party;
import cn.partytime.redis.service.RedisService;
import cn.partytime.rpcService.PartyLogicService;
import cn.partytime.service.movie.MovieService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2016/12/27.
 */
@Service
public class ScreenService {

    private static final Logger logger = LoggerFactory.getLogger(ScreenService.class);
    @Autowired
    private PartyService partyService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private MovieScheduleService movieScheduleService;

    @Autowired
    private DanmuClientService danmuClientService;


    @Autowired
    private PreDanmuHandler preDanmuHandler;

    @Autowired
    private PartyLogicService partyLogicService;

    @Autowired
    private RedisService redisService;


    @Autowired
    private MovieService movieService;

    /**
     * 获取活动信息
     * @param registCode
     * @return
     */
    public RestResultModel partyStatus(String registCode){
        RestResultModel restResultModel = new RestResultModel();
        DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);
        if (danmuClient == null) {
            logger.info("注册码:{}错误", registCode);
            restResultModel.setResult(404);
            restResultModel.setResult_msg("客户端不存在!");
            return restResultModel;
        }

        String addressId = danmuClient.getAddressId();
        PartyLogicModel partyLogicModel = partyLogicService.findPartyAddressId(addressId);
        if(partyLogicModel!=null){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("partyId",partyLogicModel.getPartyId());
            if(partyLogicModel.getActiveTime()!=null){
                map.put("time",partyLogicModel.getActiveTime().getTime());
            }else{
                map.put("time",0);
            }
            map.put("status",partyLogicModel.getStatus());
            map.put("type", PartyTypeEnmu.getEnName(partyLogicModel.getType()));
            restResultModel.setResult(200);
            restResultModel.setData(map);
            return restResultModel;
        }else{
            logger.info("活动不存在");
            restResultModel.setResult(200);
            return restResultModel;
        }
    }

    /**
     * @param registCode
     * @param command
     * @return
     */
    public RestResultModel partyStart( String registCode,String command,long clientTime) {

        Party party = partyService.findByMovieAliasOnLine(command);

        logger.info("弹幕开始请求：指令编号：{},registCode:{}", command, registCode);
        RestResultModel restResultModel = new RestResultModel();
        DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);
        restResultModel = checkClientExist(danmuClient,registCode);
        if(restResultModel!=null){
            return restResultModel;
        }
        restResultModel = checkPartyIsOk(party);
        if(restResultModel!=null){
            return restResultModel;
        }
        String addressId = danmuClient.getAddressId();
        return movieService.danmuStart(party.getId(),addressId,clientTime);

    }


    /**
     * 电影开始
     * @param registCode
     * @return
     */
    public RestResultModel moviceStart(String partyId, String registCode,long clientTime) {
        logger.info("电影开始请求：活动编号：{},registCode:{}", partyId, registCode);
        RestResultModel restResultModel = new RestResultModel();
        DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);
        restResultModel = checkClientExist(danmuClient,registCode);
        if(restResultModel!=null){
            return restResultModel;
        }
        Party party = partyService.findById(partyId);
        restResultModel = checkPartyIsOk(party);
        if(restResultModel!=null){
            return restResultModel;
        }
        String addressId = danmuClient.getAddressId();
        return movieService.movieStart(party.getId(),addressId,clientTime);
    }

    /**
     * 电影结束
     * @param registCode
     * @return
     */
    public RestResultModel moviceStop(String partyId, String registCode,long clientTime) {
        logger.info("电影结束请求：活动编号：{},registCode:{}", partyId, registCode);
        RestResultModel restResultModel = new RestResultModel();

        DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);
        restResultModel = checkClientExist(danmuClient,registCode);
        if(restResultModel!=null){
            return restResultModel;
        }
        Party party = partyService.findById(partyId);
        restResultModel = checkPartyIsOk(party);
        if(restResultModel!=null){
            return restResultModel;
        }
        restResultModel = new RestResultModel();
        String addressId = danmuClient.getAddressId();
        return movieService.movieStop(party.getId(),addressId,clientTime);
    }


    private RestResultModel checkClientExist(DanmuClient danmuClient,String registCode){
        if (danmuClient == null) {
            RestResultModel restResultModel = new RestResultModel();
            logger.info("注册码:{}错误", registCode);
            restResultModel.setResult(404);
            restResultModel.setResult_msg("客户端不存在!");
            return restResultModel;
        }
        return null;
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


    public RestResultModel sendAdStatusToClient(String name,String status,String registCode){

        logger.info("宣传片播放:{},状态:{},注册码:{}",name,status,registCode);
        DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);

        RestResultModel restResultModel = new RestResultModel();
        restResultModel = checkClientExist(danmuClient,registCode);
        if(restResultModel!=null){
            return restResultModel;
        }

        String addressId = danmuClient.getAddressId();


        Map<String,Object> commandObject = new HashMap<String,Object>();
        String key = CommandCacheKey.PUB_COMMAND_PROMOTIONALFILM_CACHE+addressId;

        commandObject.put("type", ProtocolConst.PROTOCOL_PROMOTIONALFILM);

        Map<String,Object> dataMap = new HashMap<String,Object>();

        dataMap.put("status",status);

        dataMap.put("name",name);

        commandObject.put("data",dataMap);

        String message = JSON.toJSONString(commandObject);
        logger.info("发送给服务器的客户端{}", message);
        redisService.set(key, message);
        redisService.expire(key, 60 );
        //通知客户端
        redisTemplate.convertAndSend("party:command", addressId);

        restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        return restResultModel;
    }
}

