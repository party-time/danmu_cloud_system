package cn.partytime.collector.rpcService.alarmRpc;

import cn.partytime.collector.rpcService.alarmRpc.impl.DanmuServiceHystrix;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/20.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DANMUMESSAGENOTICE,fallback = DanmuServiceHystrix.class)
public interface DanmuService {

    @RequestMapping(value = "/rpcDanmu/danmuAlarm" ,method = RequestMethod.GET)
    public void danmuAlarm(@RequestParam(value = "type") String type, @RequestParam(value = "code") String code);
}
