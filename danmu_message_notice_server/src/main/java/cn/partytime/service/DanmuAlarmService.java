package cn.partytime.service;

import cn.partytime.logicService.MessageLogicService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.service.MessageService;
import cn.partytime.message.util.MessageSystemConst;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */

@Service
public class DanmuAlarmService implements MessageService {

    @Autowired
    private MessageLogicService messageLogicService;

    @Override
    public void before(MessageObject messageObject) {
        Map<String,Object> map = (Map<String,Object>)messageObject.getData();
    }

    @Override
    public void after(MessageObject messageObject) {
        //messageLogicService.sendMessage(messageObject);
        System.out.println("================>"+JSON.toJSONString(messageObject));
    }
}
