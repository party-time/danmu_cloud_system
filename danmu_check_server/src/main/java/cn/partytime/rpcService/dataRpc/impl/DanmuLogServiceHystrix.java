package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.rpcService.dataRpc.DanmuLogService;
import cn.partytime.model.DanmuLog;
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
