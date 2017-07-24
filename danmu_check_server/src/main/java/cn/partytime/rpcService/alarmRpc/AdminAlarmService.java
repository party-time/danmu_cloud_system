package cn.partytime.rpcService.alarmRpc;

import cn.partytime.rpcService.alarmRpc.impl.AdminAlarmServiceHystrix;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ServerConst.SERVER_NAME_DANMUMESSAGENOTICE,fallback = AdminAlarmServiceHystrix.class)
public interface AdminAlarmService {

    @RequestMapping(value = "/rpcAdmin/admiOffLine" ,method = RequestMethod.GET)
    public void admiOffLine();

}
