package cn.partytime.scheduler.impl;

import cn.partytime.scheduler.BaseScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("delUpdatePlanFile")
@EnableScheduling
@RefreshScope
@Slf4j
public class DelUpdatePlanFileScheduler implements BaseScheduler {
    @Override
    public void execute() throws IOException {

    }
}
