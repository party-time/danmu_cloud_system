package cn.partytime.check.dataService;

import cn.partytime.check.dataService.impl.AdminUserServiceHystrix;
import cn.partytime.check.dataService.impl.CmdLogicServiceHystrix;
import cn.partytime.check.model.AdminUser;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/7.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = AdminUserServiceHystrix.class)
public interface AdminUserService {

    @RequestMapping(value = "/adminUserService/getAdminUser" ,method = RequestMethod.GET)
    public AdminUser getAdminUser(@RequestParam(value = "authKey") String authKey);
    
}
