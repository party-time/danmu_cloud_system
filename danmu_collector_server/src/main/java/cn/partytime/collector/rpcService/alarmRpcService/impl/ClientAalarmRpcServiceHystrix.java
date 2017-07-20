package cn.partytime.collector.rpcService.alarmRpcService.impl;

import cn.partytime.collector.rpcService.alarmRpcService.ClientAalarmRpcService;
import org.springframework.stereotype.Service;


@Service
public class ClientAalarmRpcServiceHystrix implements ClientAalarmRpcService {

    @Override
    public void clientNetError(String addressId) {

    }
}
