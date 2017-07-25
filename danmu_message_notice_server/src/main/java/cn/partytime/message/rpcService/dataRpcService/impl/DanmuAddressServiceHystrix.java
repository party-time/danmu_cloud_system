package cn.partytime.message.rpcService.dataRpcService.impl;

import cn.partytime.message.rpcService.dataRpcService.DanmuAddressService;
import cn.partytime.model.DanmuAddressDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DanmuAddressServiceHystrix implements DanmuAddressService {
    @Override
    public List<DanmuAddressDTO> findByType(Integer type) {
        return null;
    }

    @Override
    public DanmuAddressDTO findById(String id) {
        return null;
    }
}
