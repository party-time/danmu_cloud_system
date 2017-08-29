package cn.partytime.listener;

import cn.partytime.service.ClientCommandService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ClientCommandListener implements MessageListener {



    @Autowired
    private ClientCommandService clientCommandService;


    @Override
    public void onMessage(Message message, byte[] bytes) {
        if(message!=null){
            String addressId = JSON.parseObject(String.valueOf(message),String.class).replace("'","");
            log.info("get pub addressId command:{}",addressId);
            clientCommandService.pubCommandToJavaClient(addressId);
        }
    }
}
