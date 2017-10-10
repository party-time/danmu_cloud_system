package cn.partytime.logicService;

import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.DanmuClientModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.PartyModel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class CommonDataService {
    private static final Logger logger = LoggerFactory.getLogger(CommonDataService.class);

    @Autowired
    private RpcDanmuClientService rpcDanmuClientService;


    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private RpcPartyService rpcPartyService;

    public Map<String,String> setMapByAddressId(String key,String addressId,String partyId){
        Map<String, String> map = null;
        DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findById(addressId);

        log.info("danmuAddressModel:{}", JSON.toJSONString(danmuAddressModel));
        PartyModel partyModel = rpcPartyService.getPartyByPartyId(partyId);

        map = new HashMap<String, String>();
        map.put("key", key);

        map.put("addressName", danmuAddressModel.getName());
        map.put("addressId",addressId);
        if(partyModel!=null){
            map.put("partyId",partyModel.getId());
            map.put("movieName", partyModel.getName());
        }
        return map;
    }

    public Map<String,String> setMapByRegistorCode(String key,String registorCode){
        DanmuClientModel danmuClientModel = rpcDanmuClientService.findByRegistCode(registorCode);
        Map<String, String> map = null;
        if (danmuClientModel != null) {
            DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findById(danmuClientModel.getAddressId());
            map = new HashMap<String, String>();
            map.put("key", key);
            map.put("screen", danmuClientModel.getName());
            map.put("addressName", danmuAddressModel.getAddress());
            map.put("addressId",danmuAddressModel.getId());
            return map;
        }
        return null;
    }



    public Map<String,String> setCommonMapByAddressId(String key, String addressId) {
        Map<String, String> map = null;
        DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findById(addressId);
        if (danmuAddressModel == null) {
            logger.info("通过地址编号:{}，查询不到地址信息", addressId);
            return null;
        }
        String addressName = danmuAddressModel.getName();
        PartyLogicModel partyLogicModel = rpcPartyService.findFilmByAddressId(addressId);
        if (partyLogicModel == null) {
            logger.info("通过地址编号:{}，查询不到前正在进行的活动", addressId);
            return null;
        }
        String movieName = partyLogicModel.getPartyName();
        map = new HashMap<String, String>();
        map.put("key", key);
        map.put("partyId",partyLogicModel.getPartyId());
        map.put("addressName", addressName);
        map.put("addressId",addressId);
        map.put("movieName", movieName);
        return map;
    }


    public Map<String,String> setCommonMapByRegistor(String key, String code) {
        DanmuClientModel danmuClientModel = rpcDanmuClientService.findByRegistCode(code);
        Map<String, String> map = null;
        if (danmuClientModel != null) {
            String addressId = danmuClientModel.getAddressId();
            DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findById(addressId);
            if (danmuAddressModel == null) {
                logger.info("通过地址编号:{}，查询不到地址信息", addressId);
                return null;
            }
            String addressName = danmuAddressModel.getName();
            PartyLogicModel partyLogicModel = rpcPartyService.findFilmByAddressId(addressId);
            if (partyLogicModel == null) {
                logger.info("通过地址编号:{}，查询不到前正在进行的活动", addressId);
                return null;
            }

            map = new HashMap<String, String>();
            map.put("key", key);
            map.put("partyId",partyLogicModel.getPartyId());
            map.put("screen", danmuClientModel.getName());
            map.put("addressId",danmuClientModel.getAddressId());
            map.put("addressName", addressName);
            map.put("movieName", partyLogicModel.getPartyName());
            return map;

        }
        return  null;
    }

}
