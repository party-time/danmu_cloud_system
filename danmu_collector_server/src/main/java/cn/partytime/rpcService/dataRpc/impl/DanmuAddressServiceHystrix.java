package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.model.DanmuAddressDTO;
import cn.partytime.rpcService.dataRpc.DanmuAddressService;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/5.
 */

@Component
public class DanmuAddressServiceHystrix implements DanmuAddressService {

    @Override
    public DanmuAddressDTO findAddressByLonLat(Double longitude, Double latitude) {
        return null;
    }
}
