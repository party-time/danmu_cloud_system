package cn.partytime.rpcService.alarmRpc.impl;

import cn.partytime.model.Party;
import cn.partytime.rpcService.alarmRpc.MovieAlarmService;
import org.springframework.stereotype.Service;


@Service
public class MovieAlarmServiceHystrix implements MovieAlarmService {

    @Override
    public void movieTime(String partyId, String addressId, long time) {

    }
}
