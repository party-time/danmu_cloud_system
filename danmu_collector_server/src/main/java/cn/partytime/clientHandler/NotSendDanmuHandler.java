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

    @Autowired
    private DanmuCommandBussinessService danmuCommandBussinessService;


    @Autowired
    private DanmuSendService danmuSendService;

    @Autowired
    private ClientChannelService clientChannelService;

    public void danmuListenHandler(String addressId,int count) {
        try {
            long size = getNotSendQueueSize(addressId);
            if(size>0){
                //从未发送
                int countScreen = clientChannelService.findDanmuClientCountByAddressIdAndClientType(addressId,Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN));
                if(countScreen!=0) {
                    Object object = danmuCommandBussinessService.getDanmuFromNotSendQueue(addressId);
                    if(object!=null){
                        danmuSendService.sendMessageToAllClient(addressId,object);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    count++;
                }
            }
            size =getNotSendQueueSize(addressId);
            if(size>0 && count<10){
                log.info("当前队列中剩余的弹幕数:{}",size);
                log.info("未发送的弹幕队列中，弹幕数量");
                danmuListenHandler(addressId,count);
            }
        }catch (Exception e) {
            log.error("实时弹幕监听线程异常:{}", e.getMessage());
        }
    }

    public long getNotSendQueueSize(String addressId){
        return  danmuCommandBussinessService.getDanmuFromNotSendQueueSize(addressId);
    }
}
