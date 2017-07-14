package cn.partytime.rpcService.impl;

import cn.partytime.rpcService.DanmuAddressLogicService;
import cn.partytime.model.manager.DanmuAddress;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/11.
 */

@Component
public class DanmuAddressServiceHystrix implements DanmuAddressLogicService {
    @Override
    public DanmuAddress findAddressByLonLat(Double longitude, Double latitude) {
        return null;
    }
}
