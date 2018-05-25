package cn.partytime.clientHandler;

import cn.partytime.business.danmu.DanmuCommandBussinessService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by admin on 2018/5/18.
 */

@Slf4j
@Component
public class RealTimeDanmuTempCacheHandler {


    @Autowired
    private DanmuCommandBussinessService danmuCommandBussinessService;

    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public void danmuListenHandler(String addressId) {
        log.info("实时弹幕监听线程启动");
        try {
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    //danmuSendService.sendDanmu(addressId)
                    Object object = danmuCommandBussinessService.getDanmuFromPubDanmuList(addressId);
                    if(object!=null){
                        Map<String,Object> map = (Map<String,Object>) JSON.parse(String.valueOf(object));
                        danmuCommandBussinessService.pubDanmuToNotSendQueue(addressId,map);
                    }

                }
            });
        }catch (Exception e) {
            log.error("实时弹幕监听线程异常:{}", e.getMessage());
        }
    }
}
