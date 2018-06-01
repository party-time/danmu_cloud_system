package cn.partytime.rpc;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.dataRpc.RpcDanmuService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.dataRpc.RpcWechatService;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.model.DanmuModel;
import cn.partytime.model.PartyModel;
import cn.partytime.model.WechatUserDto;
import cn.partytime.model.WechatUserInfoDto;
import cn.partytime.service.DanmuAlarmService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2018/5/29.
 */

@RestController
@RequestMapping("/rpcPayDanmu")
@Slf4j
public class RpcPayDanmuAlarmService {

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private MessageHandlerService messageHandlerService;

    @Autowired
    private RpcDanmuService rpcDanmuService;

    @Autowired
    private CommonDataService commonDataService;

    @Autowired
    private DanmuAlarmService danmuAlarmService;

    @Autowired
    private RpcWechatService rpcWechatService;




    @RequestMapping(value = "/paySendErrorAlarm" ,method = RequestMethod.GET)
    public void paySendErrorAlarm(@RequestParam String addressId,@RequestParam String danmuId ) {
        log.info("表白发送失败告警");
        Map<String,String> map = new HashMap<String,String>();
        DanmuModel danmuModel =  rpcDanmuService.findById(danmuId);

        String userId = danmuModel.getCreateUserId();

        WechatUserDto wechatUserDto =  rpcWechatService.findByUserId(userId);



        log.info("弹幕信息:{}", JSON.toJSONString(danmuModel));
        map = commonDataService.setMapByAddressId(AlarmKeyConst.PAYSENDERROR,addressId,"");
        map.put("userName",wechatUserDto.getNick());

        log.info("告警map:{}",JSON.toJSONString(map));

        MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.MoneyLogCode.PAYSENDERROR,map);
        mapMessageObject.setValue(0);
        mapMessageObject.setThreshold(0);
        messageHandlerService.messageHandler(danmuAlarmService,mapMessageObject);
    }

    @RequestMapping(value = "/payNotSendAlarm" ,method = RequestMethod.GET)
    public void payNotSendAlarm(@RequestParam String addressId,@RequestParam String danmuId ) {
        log.info("表白发送失败告警");
        Map<String,String> map = new HashMap<String,String>();
        DanmuModel danmuModel =  rpcDanmuService.findById(danmuId);

        String userId = danmuModel.getCreateUserId();
        WechatUserDto wechatUserDto =  rpcWechatService.findByUserId(userId);


        log.info("弹幕信息:{}", JSON.toJSONString(danmuModel));
        map = commonDataService.setMapByAddressId(AlarmKeyConst.PARYNOTSENDERROR,addressId,"");
        map.put("userName",wechatUserDto.getNick());

        log.info("告警map:{}",JSON.toJSONString(map));

        MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.MoneyLogCode.PARYNOTSENDERROR,map);
        mapMessageObject.setValue(0);
        mapMessageObject.setThreshold(0);
        messageHandlerService.messageHandler(danmuAlarmService,mapMessageObject);
    }
}
