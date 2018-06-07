package cn.partytime.service;

import cn.partytime.redis.service.RedisService;
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

    @Autowired
    private ClientPartyService clientPartyService;

    /**
     * 发送预制弹幕
     *
     * @param addressId
     */
    public void     sendPreDanmu(String addressId,int danmuCount,String partyId) {
        int count = screenDanmuService.danmuCount(addressId,danmuCount,partyId);
        logger.info("************需要向活动:{}客户端补充的弹幕数量:{}",partyId,count);
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
                String cachePartyId = clientPartyService.findCurrentPatyId(addressId);
                logger.info("当前活动编号:{},cache中的活动编号是:{}",partyId,cachePartyId);
                if(count2>0 && partyId.equals(cachePartyId)){

                    danmuSendService.sendPreDanmu(addressId,partyId,count2);
                }
            }
        }
    }
}
