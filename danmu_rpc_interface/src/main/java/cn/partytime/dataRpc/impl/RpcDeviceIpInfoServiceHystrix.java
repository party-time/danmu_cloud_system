package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcDeviceIpInfoService;
import cn.partytime.model.DeviceIpInfoModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RpcDeviceIpInfoServiceHystrix implements RpcDeviceIpInfoService {
    @Override
    public List<DeviceIpInfoModel> findByAddressId(String addressId) {
        return null;
    }
}
