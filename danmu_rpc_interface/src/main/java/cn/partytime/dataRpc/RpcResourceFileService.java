package cn.partytime.dataRpc;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "${dataRpcServer}",fallback = RpcResourceFileService.class)
public interface RpcResourceFileService {

    @RequestMapping(value = "/rpcResourceFile/delEndPartyResourceFile" ,method = RequestMethod.GET)
    void delEndPartyResourceFile();


}
