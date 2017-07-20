package cn.partytime.rpcService.alarmRpcService;


import cn.partytime.common.util.ServerConst;
import cn.partytime.rpcService.alarmRpcService.impl.BulbServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServerConst.SERVER_NAME_DANMUMESSAGENOTICE,fallback = BulbServiceHystrix.class)
public interface BulbService {

    @RequestMapping(value = "/blubLife" ,method = RequestMethod.GET)
    public void partyStart(@RequestParam(value = "registerCode") String registerCode);
}
