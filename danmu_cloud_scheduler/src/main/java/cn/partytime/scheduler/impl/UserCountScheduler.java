package cn.partytime.scheduler.impl;


import cn.partytime.cache.user.WechatUserCountCacheService;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcWechatService;
import cn.partytime.scheduler.BaseScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("userCountScheduler")
@EnableScheduling
@RefreshScope
@Slf4j
public class UserCountScheduler  implements BaseScheduler {

    @Autowired
    private RpcWechatService rpcWechatService;

    @Scheduled(cron = "0 5 0 ? * MON")
    public void execute() throws IOException {
        log.info("执行统计用户数据定时任务");
        rpcWechatService.countNewWechatUser();
    }


}
