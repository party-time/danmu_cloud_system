package cn.partytime.clientHandler;

import cn.partytime.business.danmu.DanmuCommandBussinessService;
import cn.partytime.common.constants.ClientConst;
import cn.partytime.service.ClientChannelService;
import cn.partytime.service.DanmuSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 未发送的弹幕处理
 * Created by admin on 2018/5/23.
 */

@Slf4j
@Component
public class NotSendDanmuHandler {

    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Autowired
    private DanmuCommandBussinessService danmuCommandBussinessService;


    @Autowired
    private DanmuSendService danmuSendService;

    @Autowired
    private ClientChannelService clientChannelService;

    public void danmuListenHandler(String addressId) {
        log.info("实时弹幕监听线程启动");
        try {
            sendTempCacheDanmu(addressId);
        }catch (Exception e) {
            log.error("实时弹幕监听线程异常:{}", e.getMessage());
        }
    }


    private void sendTempCacheDanmu(String addressId){
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                long size = danmuCommandBussinessService.getNotSendQueueSize(addressId);
                if(size>0){
                    //从未发送
                    Object object = danmuCommandBussinessService.getDanmuFromNotSendQueue(addressId);
                    if(object!=null){

                        int countMobile = clientChannelService.findDanmuClientCountByAddressIdAndClientType(addressId,Integer.parseInt(ClientConst.CLIENT_TYPE_MOBILE));
                        int countScreen = clientChannelService.findDanmuClientCountByAddressIdAndClientType(addressId,Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN));

                        if(countScreen!=0){
                            danmuSendService.sendMessageToAllClient(addressId,object);
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            sendTempCacheDanmu(addressId);
                        }
                    }
                }
            }
        });
    }
}
