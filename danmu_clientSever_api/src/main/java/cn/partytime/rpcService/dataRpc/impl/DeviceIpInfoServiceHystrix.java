package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.rpcService.dataRpc.DeviceIpInfoService;
import cn.partytime.model.DeviceIpInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dm on 2017/7/12.
 */

@Component
public class DeviceIpInfoServiceHystrix implements DeviceIpInfoService {
    @Override
    public List<DeviceIpInfo> findByAddressId(String addressId) {
        return null;
    }
}
