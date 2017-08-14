package cn.partytime.logicService;

import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.DanmuClientModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.rpc.RpcMovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonDataService {
    private static final Logger logger = LoggerFactory.getLogger(CommonDataService.class);

    @Autowired
    private RpcDanmuClientService rpcDanmuClientService;


    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private RpcPartyService rpcPartyService;

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
            //map.put("movieName", movieName);
            return map;

        }
        return  null;
    }

}
