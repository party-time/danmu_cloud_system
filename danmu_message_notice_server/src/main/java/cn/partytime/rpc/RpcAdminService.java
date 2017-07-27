package cn.partytime.rpc;

import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.service.AdminIsOnLineAlarmService;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.PartyLogicModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */

@RestController
@RequestMapping("/rpcAdmin")
public class RpcAdminService {

    private static final Logger logger = LoggerFactory.getLogger(RpcAdminService.class);
    @Autowired
    private AdminIsOnLineAlarmService adminIsOnLineAlarmService;

    @Autowired
    private RpcDanmuAddressService danmuAddressService;

    @Autowired
    private MessageHandlerService messageHandlerService;

    @Autowired
    private RpcPartyService partyService;

    @RequestMapping(value = "/admiOffLine" ,method = RequestMethod.GET)
    public void admiOffLine() {
        List<DanmuAddressModel> danmuAddressList = danmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddressModel danmuAddress:danmuAddressList){
                PartyLogicModel partyLogicModel =partyService.findPartyAddressId(danmuAddress.getId());
                if(partyLogicModel!=null){
                    Map<String,Object> map = new HashMap<>();
                    MessageObject<Map<String,Object>> mapMessageObject = new MessageObject<Map<String,Object>>(LogCodeConst.AdminLogCode.ADMIN_ONLINE_COUNT_ZERO,map);
                    messageHandlerService.messageHandler(adminIsOnLineAlarmService,mapMessageObject);
                    break;
                }
            }

        }
    }

}
