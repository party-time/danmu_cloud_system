package cn.partytime.rpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.rpcService.impl.CmdLogicServiceHystrix;
import cn.partytime.model.CmdTempAllData;
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


    @RequestMapping(value = "/findCmdTempAllDataById" ,method = RequestMethod.GET)
    public CmdTempAllData findCmdTempAllDataById(@RequestParam(value = "id") String id);

}
