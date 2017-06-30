package cn.partytime.collector.service;

import cn.partytime.common.cachekey.ScreenClientCacheKey;
import cn.partytime.logic.danmu.DanmuClientModel;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by lENOVO on 2016/11/3.
 */
@Service
public class PreDanmuLogicService {

    private static final Logger logger = LoggerFactory.getLogger(PotocolService.class);
    @Autowired
    private ScreenDanmuService screenDanmuService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DanmuSendService danmuSendService;

    /**
     * 发送预制弹幕
     *
     * @param addressId
     */
    public void     sendPreDanmu(String addressId,int danmuCount,String partyId) {
        int count = screenDanmuService.danmuCount(addressId,danmuCount,partyId);
        logger.info("需要向客户端补充的弹幕数量:{}",count);
        int time = 0;
        if (count > 0) {

            for (int i = 1; i <= count; i++) {
                try {
                    Random random = new Random();
                    if(count <=5 && count>0){
                        time = random.nextInt(3);
                        Thread.sleep(time*1000);
                    }else if(count <=10 && count>5){
                        time = random.nextInt(2);
                        Thread.sleep(time*1000);
                    }else{
                        Thread.sleep(500);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int addressDanmuCount = screenDanmuService.getAddressDanmuCount(addressId);
                int count2 = screenDanmuService.danmuCount(addressId,addressDanmuCount,partyId);
                if(count2>0){
                    danmuSendService.sendPreDanmu(addressId,partyId);
                }
                /*String key = ScreenClientCacheKey.SCREEN_DANMU_COUNT+addressId;
                Object object =  redisService.get(key);
                if(object!=null){
                    DanmuClientModel danmuClientModel = JSON.parseObject(String.valueOf(object),DanmuClientModel.class);
                    int count2 = screenDanmuService.danmuCount(addressId,danmuClientModel.getDanmuCount(),partyId);
                    logger.info("向客户端补充弹幕的时候，此时客户端弹幕数:{}",count2);
                    if(count2>0){
                        danmuSendService.sendPreDanmu(addressId,partyId);
                    }else{
                        return;
                    }
                }*/
            }
        }
    }
}
