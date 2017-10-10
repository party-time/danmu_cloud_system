package cn.partytime.alarmRpc;

import cn.partytime.alarmRpc.impl.RpcDanmuAlarmServiceHystrix;
import cn.partytime.alarmRpc.impl.RpcMovieAlarmServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${alarmRpcServer}",fallback = RpcMovieAlarmServiceHystrix.class)
public interface RpcMovieAlarmService {

    @RequestMapping(value = "/rpcMovie/movieStartError" ,method = RequestMethod.GET)
    public void movieStartError(@RequestParam(value = "partyId") String partyId,@RequestParam(value = "addressId") String addressId, @RequestParam(value = "time") long time);


    @RequestMapping(value = "/rpcMovie/movieTime" ,method = RequestMethod.GET)
    public void movieTime(@RequestParam(value = "partyId") String partyId, @RequestParam(value = "addressId") String addressId, @RequestParam(value = "time") long time);
}
