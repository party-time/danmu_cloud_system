package cn.partytime.check.rpcService.impl;

import cn.partytime.check.rpcService.DanmuLogService;
import cn.partytime.check.model.DanmuLog;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/6.
 */

@Component
public class DanmuLogServiceHystrix implements DanmuLogService {

    @Override
    public DanmuLog findDanmuLogById(String id) {
        return null;
    }

    @Override
    public DanmuLog save(DanmuLog danmuLog) {
        return null;
    }
}
