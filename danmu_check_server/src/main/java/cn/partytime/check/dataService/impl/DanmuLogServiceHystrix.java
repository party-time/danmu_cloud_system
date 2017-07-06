package cn.partytime.check.dataService.impl;

import cn.partytime.check.dataService.DanmuLogService;
import cn.partytime.check.model.CmdTempAllData;
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
}
