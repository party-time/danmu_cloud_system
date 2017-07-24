package cn.partytime.message.rpcService.dataRpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.message.rpcService.dataRpcService.impl.AdminUserServiceHystrix;
import cn.partytime.model.AdminUser;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by administrator on 2017/7/24.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = AdminUserServiceHystrix.class)
public interface AdminUserService {

    @RequestMapping(value = "/findById" ,method = RequestMethod.GET)
    public AdminUser findById(@RequestParam String id);


}
