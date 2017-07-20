package cn.partytime.rpcService.dataRpcService.impl;

import cn.partytime.rpcService.dataRpcService.DanmuAddressService;
import cn.partytime.model.DanmuAddress;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@Component
public class DanmuAddressServiceHystrix implements DanmuAddressService {

    @Override
    public DanmuAddress findById(String id) {
        return null;
    }

    @Override
    public List<DanmuAddress> findByType(Integer type) {
        return null;
    }
}
