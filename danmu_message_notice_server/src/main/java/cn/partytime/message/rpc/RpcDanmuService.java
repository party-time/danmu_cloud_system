package cn.partytime.message.rpc;

import cn.partytime.common.constants.AlarmConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.messageHandlerService.DanmuAlarmService;
import cn.partytime.message.proxy.MessageHandlerService;
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

    @RequestMapping(value = "/danmuAlarm" ,method = RequestMethod.GET)
    public void danmuAlarm(@RequestParam String type, @RequestParam String code) {

        MessageObject<Map<String,Object>> mapMessageObject = null;
        Map<String,Object> map = new HashMap<>();
        switch (type) {
            case AlarmConst.DanmuAlarmType.PRE_DANMU_IS_NULL:
                System.out.println("预置弹幕没有了");
                mapMessageObject = new MessageObject<Map<String, Object>>(LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL, map);
                break;
            case AlarmConst.DanmuAlarmType.DANMU_IS_NULL:
                System.out.println("客户端没有弹幕了");
                mapMessageObject = new MessageObject<Map<String, Object>>(LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL, map);
                break;
            case AlarmConst.DanmuAlarmType.HISTORY_DANMU_IS_NULL:
                System.out.println("客户端历史弹幕没有了");
                mapMessageObject = new MessageObject<Map<String, Object>>(LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL, map);
                break;
            case AlarmConst.DanmuAlarmType.TIMER_DANMU_IS_NULL:
                System.out.println("客户端定时弹幕没有了");
                mapMessageObject = new MessageObject<Map<String, Object>>(LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL, map);
                break;
            case AlarmConst.DanmuAlarmType.DANMU_IS_MORE:
                System.out.println("客户端弹幕过量");
                mapMessageObject = new MessageObject<Map<String, Object>>(LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL, map);
                break;
        }

        sendMessage(mapMessageObject);
    }

    private void sendMessage(MessageObject<Map<String,Object>> map){
        messageHandlerService.messageHandler(danmuAlarmService,map);
    }

}
