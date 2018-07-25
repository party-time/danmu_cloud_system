package cn.partytime.logicService;


import cn.partytime.cache.wechatmin.WechatMiniCacheService;
import cn.partytime.common.cachekey.CollectorServerCacheKey;
import cn.partytime.dataRpc.RpcWechatService;
import cn.partytime.model.DanmuCollectorInfo;
import cn.partytime.model.ResultInfo;
import cn.partytime.model.ServerInfo;
import cn.partytime.model.WechatUserDto;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class WechatMiniClientLoginService {

    @Autowired
    private WechatMiniCacheService wechatMiniCacheService;


    @Autowired
    private RpcWechatService rpcWechatService;

    @Autowired
    private RedisService redisService;


    public ResultInfo findCollectorInfo(String userKey) {
        ResultInfo resultInfo = new ResultInfo();
        Object object = wechatMiniCacheService.getWechatMiniUserCache(userKey);
        if (object == null) {
            resultInfo.setCode(403);
            resultInfo.setMessage("小程序没有登录");
            return resultInfo;
        }
        String unionId = String.valueOf(object);
        WechatUserDto wechatUser = rpcWechatService.findByUnionId(unionId);
        if (wechatUser == null) {
            resultInfo.setCode(404);
            resultInfo.setMessage("无效的unionId");
            return resultInfo;
        }
        String key = CollectorServerCacheKey.COLLECTOR_SERVERLIST_CACHE_KEY;
        Set<String> collectorServerStr = redisService.getSortSetByRnage(key, 0, 1, true);
        DanmuCollectorInfo danmuCollectorInfo = null;
        if (collectorServerStr != null && !collectorServerStr.isEmpty()) {
            for (String str : collectorServerStr) {
                danmuCollectorInfo = JSON.parseObject(str, DanmuCollectorInfo.class);
                break;
            }
            if (danmuCollectorInfo != null) {
                resultInfo.setCode(200);
                resultInfo.setMessage("OK!");
                ServerInfo serverInfo = new ServerInfo();
                serverInfo.setIp(danmuCollectorInfo.getIp());
                serverInfo.setPort(danmuCollectorInfo.getPort());
                resultInfo.setServerInfo(serverInfo);
                return resultInfo;
            }
        }else{
            resultInfo.setCode(400);
            resultInfo.setMessage("没有可用的弹幕服务器!");
            return resultInfo;
        }
        return null;
    }
}
