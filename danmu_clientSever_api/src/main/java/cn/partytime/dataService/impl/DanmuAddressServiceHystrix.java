package cn.partytime.dataService.impl;

import cn.partytime.dataService.DanmuAddressService;
import cn.partytime.model.DanmuAddress;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/11.
 */

@Component
public class DanmuAddressServiceHystrix implements DanmuAddressService {

    @Override
    public DanmuAddress findById(String id) {
        return null;
    }
}
