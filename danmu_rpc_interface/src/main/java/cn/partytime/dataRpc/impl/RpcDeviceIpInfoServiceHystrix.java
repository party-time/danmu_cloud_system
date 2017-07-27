package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcDeviceIpInfoService;
import cn.partytime.model.DeviceIpInfoModel;

import java.util.List;

public class RpcDeviceIpInfoServiceHystrix implements RpcDeviceIpInfoService {
    @Override
    public List<DeviceIpInfoModel> findByAddressId(String addressId) {
        return null;
    }
}
