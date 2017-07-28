package cn.partytime.rpc;

import cn.partytime.common.constants.AlarmConst;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.DanmuClientModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.PartyModel;
import cn.partytime.service.DanmuAlarmService;
import com.sun.org.apache.regexp.internal.RE;
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
@RequestMapping("/rpcDanmu")
public class RpcDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(RpcDanmuService.class);

    @Autowired
    private DanmuAlarmService danmuAlarmService;

    @Autowired
    private MessageHandlerService messageHandlerService;


    @Autowired
    private CommonDataService commonDataService;


    @RequestMapping(value = "/danmuAlarm" ,method = RequestMethod.GET)
    public void danmuAlarm(@RequestParam String type, @RequestParam String code) {

        MessageObject<Map<String,String>> mapMessageObject = null;
        Map<String,String> map = new HashMap<>();
        switch (type) {
            case AlarmConst.DanmuAlarmType.PRE_DANMU_IS_NULL:
                System.out.println("预置弹幕没有了");
                //map.put("key", AlarmKeyConst.ALARM_KEY_PREDANMU);
                map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_PREDANMU,code);
                mapMessageObject = new MessageObject<Map<String, String>>(LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL, map);
                break;
            case AlarmConst.DanmuAlarmType.DANMU_IS_NULL:
                System.out.println("客户端没有弹幕了");
                map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_SYSTEMERROR,code);
                mapMessageObject = new MessageObject<Map<String, String>>(LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL, map);
                break;
            case AlarmConst.DanmuAlarmType.HISTORY_DANMU_IS_NULL:
                System.out.println("客户端历史弹幕没有了");
                map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_HISTORYDANMU,code);
                mapMessageObject = new MessageObject<Map<String, String>>(LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL, map);
                break;
            case AlarmConst.DanmuAlarmType.TIMER_DANMU_IS_NULL:
                System.out.println("客户端定时弹幕没有了");
                map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_TIMERDANMU,code);
                mapMessageObject = new MessageObject<Map<String, String>>(LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL, map);
                break;
            case AlarmConst.DanmuAlarmType.DANMU_IS_MORE:
                System.out.println("客户端弹幕过量");
                map = commonDataService.setCommonMapByRegistor(AlarmKeyConst.ALARM_KEY_DANMUEXCESS,code);
                mapMessageObject = new MessageObject<Map<String, String>>(LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL, map);
                break;
        }

        mapMessageObject.setValue(0);
        mapMessageObject.setThreshold(0);
        sendMessage(mapMessageObject);
    }



    private void sendMessage(MessageObject<Map<String,String>> map){
        messageHandlerService.messageHandler(danmuAlarmService,map);
    }

}
