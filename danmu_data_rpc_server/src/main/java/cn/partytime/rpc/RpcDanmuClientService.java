package cn.partytime.rpc;

import cn.partytime.model.client.DanmuClient;
import cn.partytime.service.DanmuClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dm on 2017/7/10.
 */


@RestController
@RequestMapping("/rpcDanmuClient")
public class RpcDanmuClientService {


    @Autowired
    private DanmuClientService danmuClientService;

    @RequestMapping(value = "/findByRegistCode" ,method = RequestMethod.GET)
    public DanmuClient findByRegistCode(@RequestParam String registCode) {
        return danmuClientService.findByRegistCode(registCode);
    }

}
