package cn.partytime.dataService.impl;

import cn.partytime.bean.DanmuClient;
import cn.partytime.dataService.DanmuClientService;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/4.
 */
@Component
public class DanmuClientServiceHystrix implements DanmuClientService{

    @Override
    public Integer add(Integer a, Integer b) {
        return -9999;
    }

    @Override
    public DanmuClient  findByRegistCode(String registCode) {
        return null;
    }
}
