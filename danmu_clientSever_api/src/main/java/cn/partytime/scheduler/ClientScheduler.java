package cn.partytime.scheduler;


import cn.partytime.common.util.ListUtils;
import cn.partytime.model.DanmuAddress;
import cn.partytime.model.DanmuClient;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.ProjectorAction;
import cn.partytime.rpcService.alarmRpcService.ProjectorAlarmService;
import cn.partytime.rpcService.dataRpcService.DanmuAddressService;
import cn.partytime.rpcService.dataRpcService.DanmuClientService;
import cn.partytime.rpcService.dataRpcService.ProjectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@RefreshScope
public class ClientScheduler {

    @Autowired
    private ProjectorAlarmService projectorAlarmService;

    @Autowired
    private ProjectorService projectorService;

    @Autowired
    private DanmuClientService danmuClientService;

    @Autowired
    private DanmuAddressService danmuAddressService;
    @Scheduled(cron = "0 5 2 * * ?")
    private void projectorCloseCommandListener(){

        List<DanmuAddress> danmuAddressList = danmuAddressService.findByType(1);
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
