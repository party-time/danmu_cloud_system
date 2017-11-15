package cn.partytime.alarmRpc;


import cn.partytime.alarmRpc.impl.RpcDanmuAlarmServiceHystrix;
import cn.partytime.alarmRpc.impl.RpcJavaClientAlarmServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${alarmRpcServer}",fallback = RpcJavaClientAlarmServiceHystrix.class)
public interface RpcJavaClientAlarmService {

    @RequestMapping(value = "/rpcJavaClientAlarm/javaClientException" ,method = RequestMethod.GET)
    public void javaClientAlarm(@RequestParam(value = "type") String type, @RequestParam(value = "number") Integer number, @RequestParam(value = "addressId") String addressId);
}
