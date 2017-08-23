package cn.partytime.rpc;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.service.ProjectorCloseAlarmService;
import cn.partytime.model.DanmuClientModel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */

@RestController
@RequestMapping("/rpcProjector")
@Slf4j
public class RpcProjectorAlarmService {

    private static final Logger logger = LoggerFactory.getLogger(RpcProjectorAlarmService.class);

    @Autowired
    private ProjectorCloseAlarmService projectorCloseAlarmService;

    @Autowired
    private MessageHandlerService messageHandlerService;

    @Autowired
    private RpcDanmuClientService danmuClientService;

    @Autowired
    private AlarmCacheService alarmCacheService;

    @Autowired
    private CommonDataService commonDataService;

    @RequestMapping(value = "/projectorOpen" ,method = RequestMethod.GET)
    public void projectorOpen(@RequestParam String registCode) {
        DanmuClientModel danmuClient = danmuClientService.findByRegistCode(registCode);
        logger.info("projector:{} is not open",danmuClient.getName());
        Map<String,String> map = commonDataService.setMapByRegistorCode(AlarmKeyConst.ALARM_KEY_PROJECTORNOTOPEN,registCode);
        sendMessage(LogCodeConst.DeviceLogCode.PROJECTOR_NOT_OPEN,map,registCode);
    }

    @RequestMapping(value = "/projectorClose" ,method = RequestMethod.GET)
    public void projectorClose(@RequestParam String registCode) {
        DanmuClientModel danmuClient = danmuClientService.findByRegistCode(registCode);
        logger.info("projector:{} is not close",danmuClient.getName());
        Map<String,String> map = commonDataService.setMapByRegistorCode(AlarmKeyConst.ALARM_KEY_PROJECTORNOTCLOSE,registCode);

        sendMessage(LogCodeConst.DeviceLogCode.PROJECTOR_NOT_CLOSE,map,registCode);
    }

    private void sendMessage(String type,Map<String,String> map,String registCode){
        if(map!=null){
            String addressId = map.get("addressId");
            String partyId = map.get("partyId");
            int cacheCount = alarmCacheService.findAlarmCount(addressId,type,registCode);
            if(cacheCount>0){
                log.info("电影{}告警发出的次数超过上限",type);
                return;
            }
            alarmCacheService.addAlarmCount(0,addressId,type,registCode);
            MessageObject<Map<String,String>>  mapMessageObject = new MessageObject<Map<String,String>>(type,map);
            mapMessageObject.setValue(0);
            mapMessageObject.setThreshold(0);
            messageHandlerService.messageHandler(projectorCloseAlarmService,mapMessageObject);
        }
    }

}
