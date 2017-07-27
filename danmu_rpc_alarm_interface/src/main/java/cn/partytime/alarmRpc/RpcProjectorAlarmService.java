package cn.partytime.alarmRpc;

import cn.partytime.alarmRpc.impl.RpcMovieAlarmServiceHystrix;
import cn.partytime.alarmRpc.impl.RpcProjectorAlarmServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${alarmRpcServer}",fallback = RpcProjectorAlarmServiceHystrix.class)
public interface RpcProjectorAlarmService {

    @RequestMapping(value = "/rpcProjector/projectorOpen" ,method = RequestMethod.GET)
    public void projectorOpen(@RequestParam(value = "registCode") String registCode);

    @RequestMapping(value = "/rpcProjector/projectorClose" ,method = RequestMethod.GET)
    public void projectorClose(@RequestParam(value = "registCode") String registCode);
}
