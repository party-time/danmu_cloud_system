package cn.partytime.alarmRpc;

import cn.partytime.alarmRpc.impl.RpcAdminAlarmServiceHystrix;
import cn.partytime.alarmRpc.impl.RpcBulbAlarmServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${alarmRpcServer}",fallback = RpcBulbAlarmServiceHystrix.class)
public interface RpcBulbAlarmService {

    @RequestMapping(value = "/rpcBulb/blubLife" ,method = RequestMethod.GET)
    public void partyStart(@RequestParam(value = "registerCode") String registerCode);
}
