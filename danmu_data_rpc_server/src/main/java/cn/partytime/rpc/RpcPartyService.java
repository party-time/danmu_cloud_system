package cn.partytime.rpc;

import cn.partytime.common.cachekey.FunctionControlCacheKey;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.logicService.DanmuAddressLogicService;
import cn.partytime.logicService.PartyLogicService;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.redis.service.RedisService;
import cn.partytime.repository.manager.PartyRepository;
import cn.partytime.service.*;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 2017/7/10.
 */

@RestController
@RequestMapping("/rpcParty")
public class RpcPartyService {

    private static final Logger logger = LoggerFactory.getLogger(RpcPartyService.class);

    @Autowired
    private PartyAddressRelationService partyAddressRelationService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DanmuAddressLogicService danmuAddressLogicService;

    @Autowired
    private DanmuPoolLogicService danmuPoolLogicService;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyLogicService partyLogicService;



    @RequestMapping(value = "/findByMovieAliasOnLine" ,method = RequestMethod.GET)
    public Party findByMovieAliasOnLine(@RequestParam String command){
        return partyService.findByMovieAliasOnLine(command);
    }
    @RequestMapping(value = "/findAddressIdListByPartyId" ,method = RequestMethod.GET)
    public List<String> findAddressIdListByPartyId(@RequestParam String partyId){
        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByPartyId(partyId);

        if(ListUtils.checkListIsNotNull(partyAddressRelationList)){
            List<String> addressIdList = new ArrayList<>();
            partyAddressRelationList.forEach(object -> addressIdList.add(object.getAddressId()));
            return addressIdList;
        }
        return null;
    }

    /**通过地址NO获取当前的活动*/
    @RequestMapping(value = "/getPartyByAddressId" ,method = RequestMethod.GET)
    public Party getPartyByAddressId(@RequestParam String addressId) {
        return partyLogicService.getPartyId(addressId);
    }

    /**通过活动NO获取当前的活动*/
    @RequestMapping(value = "/getPartyByPartyId" ,method = RequestMethod.GET)
    public Party getPartyByPartyId(@RequestParam String partyId) {
        return partyService.findById(partyId);
    }

    /**删除活动 @param partyId*/
    @RequestMapping(value = "/deleteParty" ,method = RequestMethod.GET)
    public void deleteParty(@RequestParam  String partyId) {
        //删除活动
        partyService.deleteById(partyId);
        //删除活动后同时删除活动与场地的关联表
        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByPartyId(partyId);
        if (null != partyAddressRelationList && partyAddressRelationList.size() > 0) {
            for (PartyAddressRelation partyAddressRelation : partyAddressRelationList) {
                String relationId = partyAddressRelation.getId();
                //删除关系
                partyAddressRelationService.del(relationId);
                //删除弹幕池
                danmuPoolLogicService.deleteDanmuPool(relationId);
            }
        }
    }

    /*验证活动是否结束*/
    @RequestMapping(value = "/checkPartyIsOver" ,method = RequestMethod.POST)
    public boolean checkPartyIsOver(@RequestBody Party party) {
        if (party.getStatus() >2) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/findPartyByLonLat" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyByLonLat(@RequestParam Double longitude, @RequestParam Double latitude){
        PartyLogicModel partyLogicModel = null;
        DanmuAddress danmuAddress = danmuAddressLogicService.findAddressByLonLat(longitude, latitude);
        logger.info("获取当前场地信息:{}", JSON.toJSONString(danmuAddress));
        //如果查询不到场地
        if (danmuAddress == null) {
            logger.info("没有获取到场地信息");
            return null;
        }
        String addressId = danmuAddress.getId();
        return findPartyAddressId(addressId);
    }

    @RequestMapping(value = "/findPartyAddressId" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyAddressId(@RequestParam String addressId){
        return partyLogicService.findPartyAddressId(addressId);
    }

    @RequestMapping(value = "/findTemporaryParty" ,method = RequestMethod.GET)
    public PartyLogicModel findTemporaryParty(@RequestParam String addressId){
        return partyLogicService.findTemporaryParty(addressId);
    }


    @RequestMapping(value = "/getPartyDmDensity" ,method = RequestMethod.GET)
    public int getPartyDmDensity(String addressId,String partyId){
        String key = FunctionControlCacheKey.FUNCITON_CONTROL_DANMU_DENSITY + partyId;
        Object object = redisService.get(key);
        int danmuDensity = 0;
        if(object!=null){
            danmuDensity = IntegerUtils.objectConvertToInt(object);
        }else{
            PartyLogicModel partyLogicModel =  findPartyAddressId(addressId);
            if(partyLogicModel!=null){
                return partyLogicModel.getDmDensity();
            }
        }
        return danmuDensity;
    }

    @RequestMapping(value = "/saveParty" ,method = RequestMethod.POST)
    public Party updateParty(@RequestBody Party party) {
        return partyRepository.save(party);
    }

}
