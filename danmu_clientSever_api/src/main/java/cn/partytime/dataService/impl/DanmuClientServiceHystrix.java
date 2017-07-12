package cn.partytime.dataService.impl;

import cn.partytime.dataService.DanmuClientService;
import cn.partytime.model.DanmuClient;
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
