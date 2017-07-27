package cn.partytime.listener;

import cn.partytime.rpc.WechatMessageRpcService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("alarmMessageListener")
public class AlarmMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(AlarmMessageListener.class);

    @Autowired
    private WechatMessageRpcService wechatMessageRpcService;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (message != null) {
            String alarmMessage = JSON.parseObject(String.valueOf(message), String.class).replace("'", "");
            Map<String,String> map = (Map<String,String>)JSON.parse(alarmMessage);
            logger.info("message:{}",alarmMessage);
            wechatMessageRpcService.send(map.get("key"),map);


        }
    }
}
