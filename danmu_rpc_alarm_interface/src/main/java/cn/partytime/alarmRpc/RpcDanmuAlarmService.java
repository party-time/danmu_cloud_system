package cn.partytime.alarmRpc;

import cn.partytime.alarmRpc.impl.RpcClientAlarmServiceHystrix;
import cn.partytime.alarmRpc.impl.RpcDanmuAlarmServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${alarmRpcServer}",fallback = RpcDanmuAlarmServiceHystrix.class)
public interface RpcDanmuAlarmService {

    @RequestMapping(value = "/rpcDanmu/danmuAlarm" ,method = RequestMethod.GET)
    public void danmuAlarm(@RequestParam(value = "type") String type, @RequestParam(value = "code") String code, @RequestParam(value = "idd") String idd);
}
