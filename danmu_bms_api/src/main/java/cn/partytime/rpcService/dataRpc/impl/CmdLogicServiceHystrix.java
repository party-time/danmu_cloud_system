package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.rpcService.CmdLogicService;
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
