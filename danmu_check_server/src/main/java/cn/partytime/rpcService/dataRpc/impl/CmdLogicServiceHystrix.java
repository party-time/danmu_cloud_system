package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.rpcService.dataRpc.CmdLogicService;
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
}
