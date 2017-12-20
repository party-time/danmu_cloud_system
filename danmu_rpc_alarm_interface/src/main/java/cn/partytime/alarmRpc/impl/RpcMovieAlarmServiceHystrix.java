package cn.partytime.alarmRpc.impl;

import cn.partytime.alarmRpc.RpcMovieAlarmService;
import org.springframework.stereotype.Component;


@Component
public class RpcMovieAlarmServiceHystrix implements RpcMovieAlarmService {
    @Override
    public void movieStartError(String partyId, String addressId, long time) {

    }

    @Override
    public void movieTime(String partyId, String addressId, long startTime, long movieStartTime) {

    }

    @Override
    public void shortTime(String partyId, String addressId, long time) {

    }


}
