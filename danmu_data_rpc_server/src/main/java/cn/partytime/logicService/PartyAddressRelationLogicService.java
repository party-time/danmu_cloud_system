package cn.partytime.logicService;

import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.service.party.PartyAddressRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lENOVO on 2016/9/28.
 */

@Service
public class PartyAddressRelationLogicService {

    @Autowired
    private PartyAddressRelationService partyAddressRelationService;

    @Autowired
    private DanmuPoolLogicService danmuPoolLogicService;


    /**
     * 通过地址编号，活动编号删除 地址 活动的关系
     *
     * @param addressId
     * @param partyId
     */
    public void deletePartyAddressRelationByAddressIdAndPartyId(String addressId, String partyId) {

        PartyAddressRelation partyAddressRelation = partyAddressRelationService.findByPartyIdAndAddressId(partyId, addressId);
        if (partyAddressRelation != null) {

            String relationId = partyAddressRelation.getId();

            partyAddressRelationService.del(relationId);

            danmuPoolLogicService.deleteDanmuPool(relationId);
        }
    }

}
