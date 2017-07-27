package cn.partytime.alarmRpc.impl;

import cn.partytime.alarmRpc.RpcClientAlarmService;
import org.springframework.stereotype.Component;

@Component
public class RpcClientAlarmServiceHystrix implements RpcClientAlarmService {
    @Override
    public void clientNetError(String addressId) {

    }
}
