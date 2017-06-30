package cn.partytime.collector.scheduler;

import cn.partytime.collector.config.DanmuChannelRepository;
import cn.partytime.collector.service.*;
import cn.partytime.common.cachekey.CollectorServerCacheKey;
import cn.partytime.common.cachekey.CommandCacheKey;
import cn.partytime.common.cachekey.ScreenClientCacheKey;
import cn.partytime.common.constants.ClientConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.common.util.LongUtils;
import cn.partytime.logic.collector.DanmuCollectorInfo;
import cn.partytime.logic.danmu.DanmuClientModel;
import cn.partytime.logic.danmu.ProtocolCommandModel;
import cn.partytime.logic.danmu.ProtocolModel;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * Created by task on 16/6/23.
 */
@Component
@Configuration
@EnableScheduling
public class DanmuServerScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DanmuServerScheduler.class);

    @Value("${netty.host}")
    private String host;

    @Value("${netty.port}")
    private int port;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private PreDanmuLogicService preDanmuLogicService;

    @Autowired
    private ClientChannelService clientChannelService;


    @Autowired
    private ScreenDanmuService screenDanmuService;

    @Autowired
    private ClientPartyService clientPartyService;


    @Autowired
    private DanmuSendService danmuSendService;

    @Scheduled(cron = "0/30 * * * * *")
    public void reportCurrentByCron() {
        logger.info("将连接的客户端数量入缓存------------>start");
        int size = danmuChannelRepository.size();
        logger.info("当前服务器连接的客户端数量:" + size);
        DanmuCollectorInfo danmuCollectorInfo = new DanmuCollectorInfo();
        logger.info("存入服务器的地址:{},端口:{}",host,port);
        danmuCollectorInfo.setIp(host);
        danmuCollectorInfo.setPort(port);
        danmuCollectorInfo.setCount(size);
        //将客户端信息写入到缓存中
        String key = CollectorServerCacheKey.COLLECTOR_SERVERLIST_CACHE_KEY;
        redisService.setSortSet(key, size, toJSONString(danmuCollectorInfo));
        redisService.expire(key, 60);
        logger.info("将连接的客户端数量入缓存------------>end");
    }


    @Scheduled(cron="0/5 * * * * ?")
    public void repeatSendCommand() {
        logger.info("重新向客户端发送新的命令");
        long time = DateUtils.getCurrentDate().getTime();
        int clientType = Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN);
        List<String> addressIdList =  clientChannelService.findScreenAddresIdList(clientType);
        if(ListUtils.checkListIsNotNull(addressIdList)){
            for(String addressId:addressIdList){

                String key = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_PRE_QUEUE_CACHE+addressId;
                Set<String> stringSet = redisService.getSortSetByRnage(key,0,1,true);
                if(stringSet!=null && stringSet.size()>0){
                    Iterator<String> it  = stringSet.iterator();
                    while (it.hasNext()){
                        Map<String, Object> commandMap = (Map<String, Object>) JSON.parse(String.valueOf(it.next()));
                        String message = JSON.toJSONString(commandMap);
                        logger.info("向客户重新发送的命令是:{}",message);
                        danmuSendService.pubDanmuToAllScreenClient(addressId,message);

                    }
                }
            }
        }
    }

    @Scheduled(cron="0/1 * * * * ?")
    public void preDanmuSchedulerSupply() {
        logger.info("预制弹幕补充逻辑定时任务开始");
        long time = DateUtils.getCurrentDate().getTime();
        int clientType = Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN);
        List<String> addressIdList =  clientChannelService.findScreenAddresIdList(clientType);
        if(ListUtils.checkListIsNotNull(addressIdList)){
            for(String addressId:addressIdList){
               long lastTime = screenDanmuService.getlastDanmuTime(addressId);
               String partyId = clientPartyService.findCurrentPatyId(addressId);
               if(StringUtils.isEmpty(partyId)){
                   logger.info("当前场地没有活动进行，补充弹幕逻终止!");
                   continue;
               }
               int count = screenDanmuService.getAddressDanmuCount(addressId);
               long subTime = time-lastTime;
                int danmuCount = screenDanmuService.danmuCount(addressId,count,partyId);
                if(danmuCount>0){
                    logger.info("预置弹幕补充逻辑:发送预置弹幕");
                    preDanmuLogicService.sendPreDanmu(addressId,count,partyId);
                }else if(subTime>10000){
                    screenDanmuService.setScreenDanmuCount(addressId,0);
                    logger.info("预置弹幕补充逻辑:发送预置弹幕");
                    preDanmuLogicService.sendPreDanmu(addressId,count,partyId);
                }
            }
        }
    }
}
