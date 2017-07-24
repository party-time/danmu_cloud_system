package cn.partytime.rpcService.alarmRpc.impl;

import cn.partytime.rpcService.alarmRpc.ClientAalarmRpcService;
import org.springframework.stereotype.Service;


@Service
public class ClientAalarmRpcServiceHystrix implements ClientAalarmRpcService {

    @Override
    public void clientNetError(String addressId) {

    }
}
