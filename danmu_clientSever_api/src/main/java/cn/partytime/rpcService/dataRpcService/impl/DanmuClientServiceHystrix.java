package cn.partytime.rpcService.dataRpcService.impl;

import cn.partytime.rpcService.dataRpcService.DanmuClientService;
import cn.partytime.model.DanmuClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */
@Component
public class DanmuClientServiceHystrix implements DanmuClientService {
    @Override
    public DanmuClient findByRegistCode(String registCode) {
        return null;
    }

    @Override
    public List<DanmuClient> findByAddressId(String addressId) {
        return null;
    }
}
