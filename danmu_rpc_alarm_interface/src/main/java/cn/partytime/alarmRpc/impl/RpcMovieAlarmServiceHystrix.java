package cn.partytime.alarmRpc.impl;

import cn.partytime.alarmRpc.RpcMovieAlarmService;
import org.springframework.stereotype.Component;


@Component
public class RpcMovieAlarmServiceHystrix implements RpcMovieAlarmService {
    @Override
    public void movieTime(String partyId, String addressId, long time) {

    }
}
