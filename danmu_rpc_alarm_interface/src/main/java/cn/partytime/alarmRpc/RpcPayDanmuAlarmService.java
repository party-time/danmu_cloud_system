package cn.partytime.alarmRpc;

import cn.partytime.alarmRpc.impl.RpcMovieAlarmServiceHystrix;
import cn.partytime.alarmRpc.impl.RpcPayDanmuAlarmServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by admin on 2018/5/29.
 */

@FeignClient(value = "${alarmRpcServer}",fallback = RpcPayDanmuAlarmServiceHystrix.class)
public interface RpcPayDanmuAlarmService {

    @RequestMapping(value = "/rpcPayDanmu/paySendErrorAlarm" ,method = RequestMethod.GET)
    public void paySendErrorAlarm(@RequestParam(value = "addressId") String addressId, @RequestParam(value = "danmuId") String danmuId );


    @RequestMapping(value = "/rpcPayDanmu/payNotSendAlarm" ,method = RequestMethod.GET)
    public void payNotSendAlarm(@RequestParam(value = "addressId") String addressId, @RequestParam(value = "danmuId") String danmuId );

}
