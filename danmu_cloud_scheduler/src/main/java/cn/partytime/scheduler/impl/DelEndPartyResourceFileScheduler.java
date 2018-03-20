package cn.partytime.scheduler.impl;

import cn.partytime.annotation.CommandAnnotation;
import cn.partytime.scheduler.BaseScheduler;
import cn.partytime.service.PartyResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("delEndPartyResourceFile")
@EnableScheduling
@RefreshScope
@Slf4j
public class DelEndPartyResourceFileScheduler implements BaseScheduler {

    @Autowired
    private PartyResourceService partyResourceService;

    //@CommandAnnotation(command = "delEndPartyResourceFile")
    @Scheduled(cron = "0 0/10 * * * ?")
    public void execute() {
        log.info("exec: ***************delEndPartyResourceFile***************");
        partyResourceService.delEndPartyResourceFile();

    }
}
