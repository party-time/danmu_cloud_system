package cn.partytime.check.dataService.impl;

import cn.partytime.check.dataService.CmdLogicService;
import cn.partytime.check.model.CmdTempAllData;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/6.
 */

@Component
public class CmdLogicServiceHystrix implements CmdLogicService {
    @Override
    public CmdTempAllData findCmdTempAllDataByIdFromCache(String templateId) {
        return null;
    }
}
