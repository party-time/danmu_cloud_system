package cn.partytime.service.wechatSession;

import cn.partytime.common.cachekey.wechat.WechatSessionKey;
import cn.partytime.model.WechatSession;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by administrator on 2016/12/1.
 */
@Service
public class WechatSessionService {

    @Autowired
    private RedisService redisService;

    public void addSession(WechatSession wechatSession){
        redisService.set(WechatSessionKey.WECHAT_SESSION_KEY+wechatSession.getOpenId(), JSON.toJSONString(wechatSession),60*60);
    }

    public WechatSession get(String openId){
        Object obj = redisService.get(WechatSessionKey.WECHAT_SESSION_KEY+openId);
        if( null != obj){
            String sessionStr = (String)obj;
            if(!StringUtils.isEmpty(sessionStr)){
                return JSON.parseObject(sessionStr,WechatSession.class);
            }
        }
        return null;
    }
}
