package cn.partytime.collector.listener;

import cn.partytime.collector.service.ClientCommandService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/4/4 0004.
 */

@Component("clientCommandListener")
public class ClientCommandListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(ClientCommandListener.class);


    @Autowired
    private ClientCommandService clientCommandService;


    @Override
    public void onMessage(Message message, byte[] bytes) {
        if(message!=null){
            String addressId = JSON.parseObject(String.valueOf(message),String.class).replace("'","");
            clientCommandService.pubCommandToJavaClient(addressId);
        }
    }
}
