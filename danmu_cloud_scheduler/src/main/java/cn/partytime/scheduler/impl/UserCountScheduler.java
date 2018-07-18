package cn.partytime.scheduler.impl;


import cn.partytime.cache.user.WechatUserCountCacheService;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcWechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("uerCountScheduler")
@EnableScheduling
@RefreshScope
@Slf4j
public class UserCountScheduler {

    @Autowired
    private RpcWechatService rpcWechatService;

    @Scheduled(cron = "0 1 0 ? * MON")
    public void countUser(){
        rpcWechatService.countNewWechatUser();
    }


}
