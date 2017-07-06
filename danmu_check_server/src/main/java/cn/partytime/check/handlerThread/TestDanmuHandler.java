package cn.partytime.check.handlerThread;

import cn.partytime.check.service.TestDanmuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by lENOVO on 2016/10/11.
 */

@Component
public class TestDanmuHandler {
    private static final Logger logger = LoggerFactory.getLogger(TestDanmuHandler.class);

    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private TestDanmuService testDanmuService;

    public void danmuListenHandler(String addressId,String partyId) {
        logger.info("测试弹幕监听线程启动");
        try {
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    testDanmuService.sendTestDanmu(addressId,partyId);
                }
            });
        }catch (Exception e) {
            logger.error("测试弹幕线程异常:{}", e.getMessage());
        }
    }

}
