package cn.partytime.rpcService.alarmRpc;


import cn.partytime.common.util.ServerConst;
import cn.partytime.model.Party;
import cn.partytime.rpcService.alarmRpc.impl.DanmuAlarmServiceHystrix;
import cn.partytime.rpcService.alarmRpc.impl.MovieAlarmServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServerConst.SERVER_NAME_DANMUMESSAGENOTICE,fallback = MovieAlarmServiceHystrix.class)
public interface MovieAlarmService {

    @RequestMapping(value = "/rpcMovie/movieTime" ,method = RequestMethod.GET)
    public void movieTime(@RequestParam(value = "partyId") String partyId, @RequestParam(value = "addressId") String addressId, @RequestParam(value = "time") long time);
}
