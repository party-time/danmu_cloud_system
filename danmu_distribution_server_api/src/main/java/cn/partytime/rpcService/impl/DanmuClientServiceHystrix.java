package cn.partytime.rpcService.impl;

import cn.partytime.rpcService.DanmuClientService;
import cn.partytime.model.DanmuClient;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/4.
 */
@Component
public class DanmuClientServiceHystrix implements DanmuClientService{

    @Override
    public DanmuClient findByRegistCode(String registCode) {
        return null;
    }
}
