package cn.partytime.rpcService.alarmRpc.impl;

import cn.partytime.rpcService.alarmRpc.ProjectorAlarmService;
import org.springframework.stereotype.Component;

@Component
public class ProjectorAlarmServiceHystrix implements ProjectorAlarmService {

    @Override
    public void projectorOpen(String name) {

    }

    @Override
    public void projectorClose(String name) {

    }
}
