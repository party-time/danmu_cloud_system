package cn.partytime.alarmRpc.impl;

import cn.partytime.alarmRpc.RpcPayDanmuAlarmService;
import org.springframework.stereotype.Component;

/**
 * Created by admin on 2018/5/29.
 */

@Component
public class RpcPayDanmuAlarmServiceHystrix implements RpcPayDanmuAlarmService {

    @Override
    public void biaobaiAlarm(String partyId, String addressId, String danmuId) {

    }
}
