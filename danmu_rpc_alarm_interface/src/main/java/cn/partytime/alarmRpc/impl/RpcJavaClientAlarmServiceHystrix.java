package cn.partytime.alarmRpc.impl;

import cn.partytime.alarmRpc.RpcJavaClientAlarmService;
import org.springframework.stereotype.Component;

@Component
public class RpcJavaClientAlarmServiceHystrix implements RpcJavaClientAlarmService {
    @Override
    public void javaClientAlarm(String type, Integer number, String addressId) {

    }
}
