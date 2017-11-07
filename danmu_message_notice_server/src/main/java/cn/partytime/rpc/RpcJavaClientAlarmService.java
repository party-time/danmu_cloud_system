package cn.partytime.rpc;


import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.model.PartyModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpcJavaClient")
@Slf4j
public class RpcJavaClientAlarmService {


    @RequestMapping(value = "/javaClientException" ,method = RequestMethod.GET)
    public void movieStartError(@RequestParam String partyId, @RequestParam String addressId, @RequestParam long time) {
        log.info("--------------java Client run Exception-------------------");
    }
}
