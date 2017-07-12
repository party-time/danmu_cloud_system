package cn.partytime.service;

import cn.partytime.common.util.DateUtils;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.PartyAddressAdRelation;
import cn.partytime.service.adDanmu.AdDanmuLibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 2017/1/18.
 */

@Service
public class BmsPartyAddressAdRelationService {
    private static final Logger logger = LoggerFactory.getLogger(BmsPartyAddressAdRelationService.class);

    @Autowired
    private PartyAddressAdRelationService partyAddressAdRelationService;

    @Autowired
    private AdDanmuLibraryService danmuLibraryService;

    public void deletePartyAddressAd(String partyId,String addressId){
        PartyAddressAdRelation  partyAddressAdRelation = partyAddressAdRelationService.findPartyAddressAdRelationByPartyIdAndAddressId(partyId,addressId);
        if(partyAddressAdRelation!=null){
            //删除广告
            partyAddressAdRelationService.deleteByPartyIdAndAddressId(partyId,addressId);
        }
    }



    public RestResultModel PartyAddressAdAdd(String partyId, String addressId, String adDanmuPoolId, String adminId){
        RestResultModel restResultModel = new RestResultModel();

        PartyAddressAdRelation addressAdRelation =partyAddressAdRelationService.findPartyAddressAdRelationByPartyIdAndAddressId(partyId,addressId);
        Date date = DateUtils.getCurrentDate();
        if(addressAdRelation ==null){
            addressAdRelation = new PartyAddressAdRelation();
            addressAdRelation.setPartyId(partyId);
            addressAdRelation.setAddressId(addressId);
            addressAdRelation.setAdDanmuPoolId(adDanmuPoolId);
            addressAdRelation.setCreateUserId(adminId);
            addressAdRelation.setUpdateUserId(adminId);
            addressAdRelation.setCreateTime(date);
            addressAdRelation.setUpdateTime(date);
            partyAddressAdRelationService.insertPartyAddressAdRelation(addressAdRelation);
        }else{
            addressAdRelation.setPartyId(partyId);
            addressAdRelation.setAddressId(addressId);
            addressAdRelation.setAdDanmuPoolId(adDanmuPoolId);
            addressAdRelation.setUpdateUserId(adminId);
            addressAdRelation.setCreateTime(date);
            partyAddressAdRelationService.updatePartyAddressAdRelation(addressAdRelation);
        }
        restResultModel.setResult(200);
        return restResultModel;
    }

}
