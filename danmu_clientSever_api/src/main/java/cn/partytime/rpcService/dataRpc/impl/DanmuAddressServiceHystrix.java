package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.model.DanmuAddressDTO;
import cn.partytime.rpcService.dataRpc.DanmuAddressService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@Component
public class DanmuAddressServiceHystrix implements DanmuAddressService {

    @Override
    public DanmuAddressDTO findById(String id) {
        return null;
    }

    @Override
    public List<DanmuAddressDTO> findByType(Integer type) {
        return null;
    }
}
