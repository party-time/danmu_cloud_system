package cn.partytime.controller;

import cn.partytime.model.RestResultModel;
import cn.partytime.service.PartyAddressRelationLogicService;
import cn.partytime.service.PartyAddressRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuwei on 2016/9/5.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/partyAddressRelation")
@Slf4j
public class PartyAddressRelationController {

    @Autowired
    private PartyAddressRelationService partyAddressRelationService;

    @Autowired
    private PartyAddressRelationLogicService partyAddressRelationLogicService;

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public RestResultModel save(String partyId, String addressId ){
        RestResultModel restResultModel = new RestResultModel();
        partyAddressRelationService.save(partyId,addressId);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/delByPartyIdAndAddressId", method = RequestMethod.GET)
    public RestResultModel delByPartyIdAndAddressId(String partyId, String addressId ){
        RestResultModel restResultModel = new RestResultModel();
        //partyAddressRelationService.delByPartyIdAndAddressId(partyId,addressId);
        partyAddressRelationLogicService.deletePartyAddressRelationByAddressIdAndPartyId(addressId,partyId);
        restResultModel.setResult(200);
        return restResultModel;
    }
}
