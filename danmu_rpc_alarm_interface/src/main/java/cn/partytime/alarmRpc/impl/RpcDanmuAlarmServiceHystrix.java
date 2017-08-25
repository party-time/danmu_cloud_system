package cn.partytime.alarmRpc.impl;

import cn.partytime.alarmRpc.RpcDanmuAlarmService;
import org.springframework.stereotype.Component;

@Component
public class RpcDanmuAlarmServiceHystrix implements RpcDanmuAlarmService {

    @Override
    public void danmuAlarm(String type, String code, String idd) {

    }
}
