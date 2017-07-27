package cn.partytime.alarmRpc;

import cn.partytime.alarmRpc.impl.RpcBulbAlarmServiceHystrix;
import cn.partytime.alarmRpc.impl.RpcClientAlarmServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${alarmRpcServer}",fallback = RpcClientAlarmServiceHystrix.class)
public interface RpcClientAlarmService {

    @RequestMapping(value = "/rpcClient/clientNetError" ,method = RequestMethod.GET)
    public void clientNetError(@RequestParam(value = "addressId") String addressId);

}
