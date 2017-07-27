package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcAdminServiceHystrix;
import cn.partytime.model.AdminUserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/10.
 */

@FeignClient(value = "${dataRpcServer}",fallback = RpcAdminServiceHystrix.class)
public interface RpcAdminService {

    /**
     * 检查authkey是否存在
     * @param authKey
     * @return
     */

    @RequestMapping(value = "/rpcAdmin/checkAuthKey" ,method = RequestMethod.GET)
    public Boolean checkAuthKey(@RequestParam(value = "authKey") String authKey);


    /**
     * 根据authKey获得管理员
     * @param authKey
     * @return
     */
    @RequestMapping(value = "/rpcAdmin/getAdminUser" ,method = RequestMethod.GET)
    public AdminUserDto getAdminUser(@RequestParam(value = "authKey") String authKey);
}
