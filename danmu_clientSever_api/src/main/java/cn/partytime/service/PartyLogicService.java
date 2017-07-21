package cn.partytime.service;

import cn.partytime.common.util.PartyTypeEnmu;
import cn.partytime.model.DanmuClient;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.rpcService.dataRpc.DanmuClientService;
import cn.partytime.rpcService.dataRpc.PartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */

@Service
public class PartyLogicService {

    private static final Logger logger = LoggerFactory.getLogger(PartyLogicService.class);

    @Autowired
    private DanmuClientService danmuClientService;

    @Autowired
    private PartyService partyService;


    public RestResultModel partyStatus(@RequestParam String registCode){
        RestResultModel restResultModel = new RestResultModel();

        logger.info("execute rpc /rpcDanmuClient/findByRegistCode");
        DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);

        if (danmuClient == null) {
            logger.info("注册码:{}错误", registCode);
            restResultModel.setResult(404);
            restResultModel.setResult_msg("客户端不存在!");
            return restResultModel;
        }

        String addressId = danmuClient.getAddressId();
        PartyLogicModel partyLogicModel = partyService.findPartyAddressId(addressId);
        if(partyLogicModel!=null){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("partyId",partyLogicModel.getPartyId());
            if(partyLogicModel.getActiveTime()!=null){
                map.put("time",partyLogicModel.getActiveTime().getTime());
            }else{
                map.put("time",0);
            }
            map.put("status",partyLogicModel.getStatus());
            map.put("type", PartyTypeEnmu.getEnName(partyLogicModel.getType()));
            restResultModel.setResult(200);
            restResultModel.setData(map);
            return restResultModel;
        }else{
            logger.info("活动不存在");
            restResultModel.setResult(200);
            return restResultModel;
        }
    }
}
