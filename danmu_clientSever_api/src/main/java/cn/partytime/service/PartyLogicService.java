package cn.partytime.service;

import cn.partytime.common.util.PartyTypeEnmu;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.DanmuClientModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.RestResultModel;
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
    private RpcDanmuClientService rpcDanmuClientService;

    @Autowired
    private RpcPartyService rpcPartyService;


    public RestResultModel partyStatus(@RequestParam String registCode){
        RestResultModel restResultModel = new RestResultModel();

        logger.info("execute rpc /rpcDanmuClient/findByRegistCode");
        DanmuClientModel danmuClient = rpcDanmuClientService.findByRegistCode(registCode);

        if (danmuClient == null) {
            logger.info("注册码:{}错误", registCode);
            restResultModel.setResult(404);
            restResultModel.setResult_msg("客户端不存在!");
            return restResultModel;
        }

        String addressId = danmuClient.getAddressId();
        PartyLogicModel partyLogicModel = rpcPartyService.findPartyByAddressId(addressId);
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
