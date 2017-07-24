package cn.partytime.scheduler;


import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.model.*;
import cn.partytime.rpcService.alarmRpc.MovieAlarmService;
import cn.partytime.rpcService.alarmRpc.ProjectorAlarmService;
import cn.partytime.rpcService.dataRpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
@RefreshScope
public class ClientScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ClientScheduler.class);

    @Autowired
    private ProjectorAlarmService projectorAlarmService;

    @Autowired
    private ProjectorService projectorService;

    @Autowired
    private DanmuClientService danmuClientService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private MovieScheduleService movieScheduleService;

    @Autowired
    private MovieAlarmService movieAlarmService;

    @Scheduled(cron = "0 0/5 * * * ?")
    private void moviePlayTimeListener(){
        logger.info("movie time is too long listener");
        List<DanmuAddress> danmuAddressList = danmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddress danmuAddress:danmuAddressList){
                String addressId = danmuAddress.getId();
                PartyLogicModel partyLogicModel  = partyService.findPartyAddressId(addressId);
                if(partyLogicModel!=null){
                    String partyId = partyLogicModel.getPartyId();
                    List<MovieSchedule>  movieScheduleList =  movieScheduleService.findByPartyIdAndAddressId(partyId,addressId);
                    if(ListUtils.checkListIsNotNull(movieScheduleList)){
                        MovieSchedule movieSchedule = movieScheduleList.get(0);
                        if(movieSchedule.getEndTime()==null){
                            Date currentDate = DateUtils.getCurrentDate();
                            Date danmuStartTime = movieSchedule.getStartTime();
                            Date movieStartTime = movieSchedule.getMoviceStartTime();
                            if(movieStartTime!=null){
                                long time = currentDate.getTime() - movieStartTime.getTime();
                                movieAlarmService.movieTime(partyId,addressId,time);
                            }
                        }
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 5 2 * * ?")
    private void projectorCloseCommandListener(){

        List<DanmuAddress> danmuAddressList = danmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddress danmuAddress:danmuAddressList){
                List<DanmuClient>  danmuClientList = danmuClientService.findByAddressId(danmuAddress.getId());
                if(ListUtils.checkListIsNotNull(danmuClientList)){
                    for(DanmuClient danmuClient:danmuClientList){
                        String regsitrorCode = danmuClient.getRegistCode();
                        PageResultModel<ProjectorAction> projectorActions =  projectorService.findProjectorActionPage(regsitrorCode,0,1);
                        List<ProjectorAction> projectorActionList = projectorActions.getRows();
                        if(projectorActionList!=null){
                            ProjectorAction projectorAction = projectorActionList.get(0);
                            if(projectorAction.getEndTime()==null){
                                projectorAlarmService.projectorClose(regsitrorCode);
                            }
                        }
                    }
                }
            }
        }

    }

}
