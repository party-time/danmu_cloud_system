package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcCmdServiceHystrix;
import cn.partytime.model.CmdTempAllData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/10.
 */

@FeignClient(name = "${dataRpcServer}",fallback = RpcCmdServiceHystrix.class)
public interface RpcCmdService {


    @RequestMapping(value = "/rpcCmd/findCmdTempAllDataByIdFromCache" ,method = RequestMethod.GET)
    public CmdTempAllData findCmdTempAllDataByIdFromCache(@RequestParam(value = "templateId") String templateId);


    @RequestMapping(value = "/rpcCmd/findCmdTempAllDataByKeyFromCache" ,method = RequestMethod.GET)
    public CmdTempAllData findCmdTempAllDataByKeyFromCache(@RequestParam(value = "key") String key);


    @RequestMapping(value = "/rpcCmd/findCmdTempAllDataById" ,method = RequestMethod.GET)
    public CmdTempAllData findCmdTempAllDataById(@RequestParam(value = "id") String id);
}
