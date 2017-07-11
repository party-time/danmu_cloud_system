package cn.partytime.dataService.impl;

import cn.partytime.dataService.CmdLogicService;
import cn.partytime.model.CmdTempAllData;
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

    @Override
    public CmdTempAllData findCmdTempAllDataById(String id) {
        return null;
    }
}
