package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.model.CmdTempAllData;
import cn.partytime.rpcService.dataRpc.CmdService;
import org.springframework.stereotype.Component;

@Component
public class CmdServiceHystrix implements CmdService {
    @Override
    public CmdTempAllData findCmdTempAllDataByIdFromCache(String templateId) {
        return null;
    }
}
