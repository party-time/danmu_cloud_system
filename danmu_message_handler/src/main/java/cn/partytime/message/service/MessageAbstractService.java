package cn.partytime.message.service;

import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.util.MessageSystemConst;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class MessageAbstractService implements MessageService  {

    @Override
    public void before(MessageObject messageObject) {
        Map<String,Object> map = new HashMap<>();
        messageObject.setData(map);
    }

    @Override
    public void after(MessageObject messageObject) {
        Map<String,Object> map = ( Map<String,Object>)messageObject.getData();
        //redisTemplate.convertAndSend(MessageSystemConst.CacheKey.MESSAGE_CACHE_KEY, JSON.toJSONString(map));
    }
}
