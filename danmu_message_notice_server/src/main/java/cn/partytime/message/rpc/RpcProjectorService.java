package cn.partytime.message.rpc;

import cn.partytime.message.messageHandlerService.ProjectorCloseAlarmService;
import cn.partytime.message.model.Party;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dm on 2017/7/19.
 */

@RestController
@RequestMapping("/rpcProjector")
public class RpcProjectorService {

    private static final Logger logger = LoggerFactory.getLogger(RpcProjectorService.class);

    @Autowired
    private ProjectorCloseAlarmService projectorCloseAlarmService;

    @RequestMapping(value = "/projectorOpen" ,method = RequestMethod.GET)
    public void projectorOpen(@RequestParam String registCode) {

    }

    @RequestMapping(value = "/projectorClose" ,method = RequestMethod.GET)
    public void projectorClose(@RequestParam String registCode) {

    }

}
