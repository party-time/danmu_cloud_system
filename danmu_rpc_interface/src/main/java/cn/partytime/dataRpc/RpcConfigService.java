package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcConfigServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${dataRpcServer}",fallback = RpcConfigServiceHystrix.class)
public interface RpcConfigService {

    @RequestMapping(value = "/rpcConfig/getUploadPath" ,method = RequestMethod.GET)
    String getUploadPath();

    @RequestMapping(value = "/rpcConfig/getDownloadPath" ,method = RequestMethod.GET)
    String getDownloadPath();

    @RequestMapping(value = "/rpcConfig/getBackupPath" ,method = RequestMethod.GET)
    String getBackupPath();
}
