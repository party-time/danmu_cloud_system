package cn.partytime.rpc;

import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.service.BulbLifeAlarmService;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.model.DanmuClientModel;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/rpcBulb")
@Slf4j
public class RpcBulbAlarmService {

    private static final Logger logger = LoggerFactory.getLogger(RpcBulbAlarmService.class);
    @Autowired
    private BulbLifeAlarmService bulbLifeAlarmService;

    @Autowired
    private RpcDanmuClientService danmuClientService;

    @Autowired
    private MessageHandlerService messageHandlerService;

    @Autowired
    private CommonDataService commonDataService;



    @RequestMapping(value = "/blubLife" ,method = RequestMethod.GET)
    public void blubLife(@RequestParam String registerCode) {
        log.info("灯泡使用寿命报警:{}",registerCode);
        DanmuClientModel danmuClient = danmuClientService.findByRegistCode(registerCode);
        Map<String,String> map = commonDataService.setMapByRegistorCode(AlarmKeyConst.ALARM_KEY_LIGHTLIFE,registerCode);
        if(map!=null){
            MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.CLientLogCode.BULB_LIFE_TIME,map);
            mapMessageObject.setValue(0);
            mapMessageObject.setValue(0);
            messageHandlerService.messageHandler(bulbLifeAlarmService,mapMessageObject);
        }

    }
}
