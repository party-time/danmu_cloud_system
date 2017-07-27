package cn.partytime.rpc;

import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.service.ProjectorCloseAlarmService;
import cn.partytime.model.DanmuClientModel;
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

    @Autowired
    private RpcDanmuClientService danmuClientService;


    @RequestMapping(value = "/projectorOpen" ,method = RequestMethod.GET)
    public void projectorOpen(@RequestParam String registCode) {
        DanmuClientModel danmuClient = danmuClientService.findByRegistCode(registCode);
        logger.info("projector:{} is not open",danmuClient.getName());
    }

    @RequestMapping(value = "/projectorClose" ,method = RequestMethod.GET)
    public void projectorClose(@RequestParam String registCode) {
        DanmuClientModel danmuClient = danmuClientService.findByRegistCode(registCode);
        logger.info("projector:{} is not close",danmuClient.getName());
    }

}
