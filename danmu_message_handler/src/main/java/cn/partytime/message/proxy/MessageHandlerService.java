package cn.partytime.message.proxy;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.service.MessageService;
import cn.partytime.message.util.MessageSystemConst;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by dm on 2017/6/29.
 */


@Component
public class MessageHandlerService {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerService.class);


    @Value("serverName")
    private String systemName;


    public void messageHandler(MessageService messageService, MessageObject messageObject){
        // 实例化InvocationHandler
        MessageInvocationHandler invocationHandler = new MessageInvocationHandler(messageService);

        // 根据目标对象生成代理对象
        MessageService proxy = (MessageService) invocationHandler.getProxy();
        //决定是否触发回调

        init(messageObject);

        //回调前处理
        proxy.before(messageObject);

        int threshold = messageObject.getThreshold();
        int value = messageObject.getValue();

        if(value>=threshold && threshold!=-1){
            //触发回调
            proxy.after(messageObject);
        }
        logger.info(JSON.toJSONString(messageObject));
    }


    private void init(MessageObject messageObject){
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
            String ip=addr.getHostAddress().toString(); //获取本机ip
            Map<String,Object> systemInfo = new HashMap<>();
            systemInfo.put(MessageSystemConst.SystemInfo.IP_KEY,ip);
            systemInfo.put(MessageSystemConst.SystemInfo.NAME_KEY,systemName);
            messageObject.setSystemInfo(systemInfo);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
