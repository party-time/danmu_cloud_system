package cn.partytime.message.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dm on 2017/7/19.
 */

@RestController
@RequestMapping("/rpcClient")
public class RpcClientService {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientService.class);

    @RequestMapping(value = "/clientNetError" ,method = RequestMethod.GET)
    public void clientNetError(@RequestParam String addressId) {

    }
}
