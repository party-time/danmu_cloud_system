package cn.partytime.rpcService.alarmRpc;


import cn.partytime.rpcService.alarmRpc.impl.ClientAalarmRpcServiceHystrix;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServerConst.SERVER_NAME_DANMUMESSAGENOTICE,fallback = ClientAalarmRpcServiceHystrix.class)
public interface ClientAalarmRpcService {

    @RequestMapping(value = "/rpcClient/clientNetError" ,method = RequestMethod.GET)
    public void clientNetError(@RequestParam(value = "addressId") String addressId);


}
