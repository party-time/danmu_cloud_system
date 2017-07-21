package cn.partytime.rpcService.alarmRpc;


import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServerConst.SERVER_NAME_DANMUMESSAGENOTICE,fallback = ProjectorAlarmService.class)
public interface ProjectorAlarmService {

    @RequestMapping(value = "/rpcProjector/projectorOpen" ,method = RequestMethod.GET)
    public void projectorOpen(@RequestParam(value = "registCode") String registCode);

    @RequestMapping(value = "/rpcProjector/projectorClose" ,method = RequestMethod.GET)
    public void projectorClose(@RequestParam(value = "registCode") String registCode);
}
