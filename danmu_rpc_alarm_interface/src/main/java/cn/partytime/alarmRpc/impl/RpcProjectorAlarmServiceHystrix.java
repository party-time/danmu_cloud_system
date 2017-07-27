package cn.partytime.alarmRpc.impl;

import cn.partytime.alarmRpc.RpcProjectorAlarmService;
import org.springframework.stereotype.Component;

@Component
public class RpcProjectorAlarmServiceHystrix implements RpcProjectorAlarmService {
    @Override
    public void projectorOpen(String registCode) {

    }

    @Override
    public void projectorClose(String registCode) {

    }
}
