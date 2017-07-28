package cn.partytime.logicService;


import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.util.MessageSystemConst;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageLogicService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void sendMessage(MessageObject messageObject){
        Map<String,Object> map = (Map<String,Object>)messageObject.getData();
        redisTemplate.convertAndSend(MessageSystemConst.CacheKey.MESSAGE_CACHE_KEY, JSON.toJSONString(map));
    }


}
