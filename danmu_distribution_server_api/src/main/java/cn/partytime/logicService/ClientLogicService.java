package cn.partytime.logicService;

import cn.partytime.common.cachekey.CollectorServerCacheKey;
import cn.partytime.dataService.DanmuClientService;
import cn.partytime.model.DanmuClient;
import cn.partytime.model.DanmuCollectorInfo;
import cn.partytime.model.ResultInfo;
import cn.partytime.model.ServerInfo;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 弹幕分发器终端逻辑
 * Created by task on 2016/6/21.
 */

@Service
public class ClientLogicService {

    private static final Logger logger = LoggerFactory.getLogger(ClientLogicService.class);

    @Autowired
    private DanmuClientService danmuClientService;

    @Autowired
    private RedisService redisService;

    //@Autowired
    //private DanmuClientService danmuClientService;


    public ResultInfo findCollectorInfo(String danmuRegistCode) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            logger.info("当前请求的注册码是:{}",danmuRegistCode);
            //通过注册码获取客户端信息
            DanmuClient danmuClient =  danmuClientService.findByRegistCode(danmuRegistCode);
            if (danmuClient == null) {
                logger.info("客户端不存在,无效的编号:{}",danmuRegistCode);
                resultInfo.setCode(404);
                resultInfo.setMessage("无效的客户端编号");
                return resultInfo;
            }

            String key = CollectorServerCacheKey.COLLECTOR_SERVERLIST_CACHE_KEY;
            Set<String> collectorServerStr = redisService.getSortSetByRnage(key, 0, 1, true);

            logger.info("当前在线的服务器列表:{}",collectorServerStr);
            DanmuCollectorInfo danmuCollectorInfo = null;


            if (collectorServerStr != null && !collectorServerStr.isEmpty()) {
                for (String str : collectorServerStr) {
                    danmuCollectorInfo = JSON.parseObject(str, DanmuCollectorInfo.class);
                    break;
                }
                if (danmuCollectorInfo != null) {
                    logger.info("服务器信息:{}",JSON.toJSONString(danmuCollectorInfo));
                    resultInfo.setCode(200);
                    resultInfo.setMessage("OK!");

                    ServerInfo serverInfo = new ServerInfo();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("ip", danmuCollectorInfo.getIp());
                    map.put("port", danmuCollectorInfo.getPort());
                    serverInfo.setPort(danmuCollectorInfo.getPort());
                    serverInfo.setIp(danmuCollectorInfo.getIp());
                    resultInfo.setServerInfo(serverInfo);
                    return resultInfo;
                }
            }else{
                resultInfo.setCode(400);
                resultInfo.setMessage("没有可用的弹幕服务器!");
                return resultInfo;
            }
        } catch (Exception e) {
            logger.error("获取弹幕服务器异常:"+e.getMessage());
            resultInfo.setCode(500);
            resultInfo.setMessage("server error");
            return resultInfo;
        }
        return null;
    }

}
