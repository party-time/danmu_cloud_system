package cn.partytime.rpc;

import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.service.ProjectorCloseAlarmService;
import cn.partytime.model.DanmuClientModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
    private MessageHandlerService messageHandlerService;

    @Autowired
    private RpcDanmuClientService danmuClientService;

    @Autowired
    private CommonDataService commonDataService;

    @RequestMapping(value = "/projectorOpen" ,method = RequestMethod.GET)
    public void projectorOpen(@RequestParam String registCode) {
        DanmuClientModel danmuClient = danmuClientService.findByRegistCode(registCode);
        logger.info("projector:{} is not open",danmuClient.getName());
        Map<String,String> map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_PROJECTOROPEN,registCode);
        MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.DeviceLogCode.PROJECTOR_OPEN_FAIL,map);
        messageHandlerService.messageHandler(projectorCloseAlarmService,mapMessageObject);

    }

    @RequestMapping(value = "/projectorClose" ,method = RequestMethod.GET)
    public void projectorClose(@RequestParam String registCode) {
        DanmuClientModel danmuClient = danmuClientService.findByRegistCode(registCode);
        logger.info("projector:{} is not close",danmuClient.getName());
        Map<String,String> map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_PROJECTORCLOSE,registCode);
        MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.DeviceLogCode.PROJECTOR_CLOSE_FAIL,map);
        messageHandlerService.messageHandler(projectorCloseAlarmService,mapMessageObject);
    }

}
