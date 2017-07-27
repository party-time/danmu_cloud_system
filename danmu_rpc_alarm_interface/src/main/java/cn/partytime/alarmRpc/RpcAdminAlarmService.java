package cn.partytime.alarmRpc;

import cn.partytime.alarmRpc.impl.RpcAdminAlarmServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${alarmRpcServer}",fallback = RpcAdminAlarmServiceHystrix.class)
public interface RpcAdminAlarmService {


    @RequestMapping(value = "/rpcAdmin/admiOffLine" ,method = RequestMethod.GET)
    public void admiOffLine();

}
