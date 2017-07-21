package cn.partytime.rpcService.dataRpc;

import cn.partytime.common.util.ServerConst;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.rpcService.dataRpc.impl.AdTimerServiceHystrix;
import cn.partytime.rpcService.dataRpc.impl.CmdServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = CmdServiceHystrix.class)
public interface CmdService {

    @RequestMapping(value = "/rpcCmd/findCmdTempAllDataByIdFromCache" ,method = RequestMethod.GET)
    public CmdTempAllData findCmdTempAllDataByIdFromCache(@RequestParam(value = "templateId") String templateId);
}
