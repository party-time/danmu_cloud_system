package cn.partytime.collector.rpcService.dataRpcService.impl;

import cn.partytime.collector.rpcService.dataRpcService.DanmuAddressService;
import cn.partytime.collector.model.DanmuAddress;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/5.
 */

@Component
public class DanmuAddressServiceHystrix implements DanmuAddressService {

    @Override
    public DanmuAddress findAddressByLonLat(Double longitude, Double latitude) {
        return null;
    }
}
