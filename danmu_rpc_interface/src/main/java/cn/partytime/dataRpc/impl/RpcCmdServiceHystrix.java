package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcCmdService;
import cn.partytime.model.CmdTempAllData;

public class RpcCmdServiceHystrix implements RpcCmdService {
    @Override
    public CmdTempAllData findCmdTempAllDataByIdFromCache(String templateId) {
        return null;
    }

    @Override
    public CmdTempAllData findCmdTempAllDataByKeyFromCache(String key) {
        return null;
    }

    @Override
    public CmdTempAllData findCmdTempAllDataById(String id) {
        return null;
    }
}
