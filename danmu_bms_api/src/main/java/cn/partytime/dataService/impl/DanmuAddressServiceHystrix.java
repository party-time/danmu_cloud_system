package cn.partytime.dataService.impl;

import cn.partytime.dataService.DanmuAddressLogicService;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.service.DanmuAddressService;
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
