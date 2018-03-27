package cn.partytime.dataRpc;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${dataRpcServer}",fallback = RpcDelUpdatePlanFileService.class)
public interface RpcDelUpdatePlanFileService {

    @RequestMapping(value = "/rpcDelUpdatePlanFile/delUpdatePlanFile" ,method = RequestMethod.GET)
    public void delUpdatePlanFile();
}
