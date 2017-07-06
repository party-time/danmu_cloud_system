package cn.partytime.logicService;

import cn.partytime.model.danmu.DanmuModel;
import cn.partytime.model.danmu.DanmuPool;
import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.service.DanmuPoolService;
import cn.partytime.service.DanmuService;
import cn.partytime.service.PartyAddressRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lENOVO on 2016/9/26.
 */

@Service
public class DanmuPoolLogicService {

    @Autowired
    private PartyAddressRelationService partyAddressRelationService;

    @Autowired
    private DanmuPoolService danmuPoolService;

    @Autowired
    private DanmuService danmuService;


    /**
     * 通过活动编号，地址编号获取弹幕池信息
     *
     * @param partyId
     * @param addressId
     * @return
     */
    public List<String> findDanmuPoolListByPartyIdAndAddressId(String partyId, String addressId) {
        PartyAddressRelation partyAddressRelation = partyAddressRelationService.findByPartyIdAndAddressId(partyId, addressId);
        DanmuPool danmuPool = danmuPoolService.findByPartyAddressRelationId(partyAddressRelation.getId());
        return danmuPool.getDanmuPoolIdList();
    }

    /**
     * 通过关系删除弹幕池
     * @param partyAddressRelationId
     */
    public void deleteDanmuPool(String partyAddressRelationId) {
        DanmuPool danmuPool = danmuPoolService.findByPartyAddressRelationId(partyAddressRelationId);
        if(danmuPool!=null){
            List<DanmuModel> danmuModelList =  danmuService.findByDanmuPoolId(danmuPool.getId());
            if(danmuModelList!=null){
                danmuPoolService.deleteById(danmuPool.getId());
            }
        }
    }

}
