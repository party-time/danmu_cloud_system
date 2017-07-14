package cn.partytime.check.rpcService;

import cn.partytime.check.rpcService.impl.CmdLogicServiceHystrix;
import cn.partytime.check.model.CmdTempAllData;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/6.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = CmdLogicServiceHystrix.class)
public interface CmdLogicService {

    @RequestMapping(value = "/rpcCmd/findCmdTempAllDataByIdFromCache" ,method = RequestMethod.GET)
    public CmdTempAllData findCmdTempAllDataByIdFromCache(@RequestParam(value = "templateId") String templateId);


}
