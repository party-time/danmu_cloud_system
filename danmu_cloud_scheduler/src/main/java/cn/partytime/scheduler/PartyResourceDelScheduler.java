package cn.partytime.scheduler;

import cn.partytime.dataRpc.RpcPartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RefreshScope
@Slf4j
public class PartyResourceDelScheduler {



    @Scheduled(cron = "0/30 * * * * ?")
    private void delPartyResourceFile() {


    }
}
