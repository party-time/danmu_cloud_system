package cn.partytime.message.rpcService.dataRpcService.impl;


import cn.partytime.message.model.DanmuClient;
import cn.partytime.message.rpcService.dataRpcService.DanmuClientService;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/11.
 */
@Component
public class DanmuClientServiceHystrix implements DanmuClientService {
    @Override
    public DanmuClient findByRegistCode(String registCode) {
        return null;
    }
}
