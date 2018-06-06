package cn.partytime.listener;

import cn.partytime.business.danmu.DanmuCommandBussinessService;
import cn.partytime.clientHandler.RealTimeDanmuHandler;
import cn.partytime.clientHandler.RealTimeDanmuTempCacheHandler;
import cn.partytime.service.ClientChannelService;
import cn.partytime.common.constants.ClientConst;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;


@Component("realTimeDanmuListener")
public class RealTimeDanmuListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RealTimeDanmuListener.class);
    @Autowired
    private RealTimeDanmuHandler realTimeDanmuHandler;

    @Autowired
    private ClientChannelService clientChannelService;

    @Autowired
    private RealTimeDanmuTempCacheHandler realTimeDanmuTempCacheHandler;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        if(message!=null){
            String addressId = JSON.parseObject(String.valueOf(message),String.class).replace("'","");
            int countMobile = clientChannelService.findDanmuClientCountByAddressIdAndClientType(addressId,Integer.parseInt(ClientConst.CLIENT_TYPE_MOBILE));
            int countScreen = clientChannelService.findDanmuClientCountByAddressIdAndClientType(addressId,Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN));
            if(countScreen==0){
                logger.info("地址:{}客户端都不在线,此时将弹幕缓存到临时队列中",addressId);
                //return;
                realTimeDanmuTempCacheHandler.danmuListenHandler(addressId);
            }else{
                realTimeDanmuHandler.danmuListenHandler(addressId);
            }
        }

    }
}