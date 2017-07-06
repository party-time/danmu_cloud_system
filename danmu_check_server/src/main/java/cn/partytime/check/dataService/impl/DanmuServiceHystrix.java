package cn.partytime.check.dataService.impl;

import cn.partytime.check.dataService.DanmuService;
import cn.partytime.check.model.DanmuModel;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/6.
 */

@Component
public class DanmuServiceHystrix implements DanmuService {
    @Override
    public DanmuModel findById(String id) {
        return null;
    }
}
