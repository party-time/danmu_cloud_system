package cn.partytime.scheduler.impl;

import cn.partytime.dataRpc.RpcDelUpdatePlanFileService;
import cn.partytime.scheduler.BaseScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("delUpdatePlanFile")
@EnableScheduling
@RefreshScope
@Slf4j
public class DelUpdatePlanFileScheduler implements BaseScheduler {

    @Autowired
    private RpcDelUpdatePlanFileService rpcDelUpdatePlanFileService;

    @Override
    @Scheduled(cron = "0 15 5 * * ?")
    public void execute() throws IOException {
        rpcDelUpdatePlanFileService.delUpdatePlanFile();
    }
}
