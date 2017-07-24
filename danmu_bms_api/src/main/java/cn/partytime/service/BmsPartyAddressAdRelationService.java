package cn.partytime.service;

import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.common.util.*;
import cn.partytime.logic.danmu.PotocolTimerDanmu;
import cn.partytime.logic.danmu.ProtocolModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.danmu.AdDanmuLibrary;
import cn.partytime.model.danmu.AdTimerDanmu;
import cn.partytime.model.danmu.TimerDanmu;
import cn.partytime.model.manager.AdTimerDanmuFile;
import cn.partytime.model.manager.PartyAddressAdRelation;
import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.model.manager.TimerDanmuFile;
import cn.partytime.repository.manager.AdTimerDanmuFileRepository;
import cn.partytime.service.adDanmu.AdDanmuLibraryService;
import cn.partytime.service.adDanmu.AdTimerDanmuFileService;
import cn.partytime.service.adDanmu.AdTimerDanmuService;
import cn.partytime.service.adDanmu.PartyAddressAdRelationService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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



    public RestResultModel PartyAddressAdAdd(String partyId, String addressId, String adDanmuPoolId,String adminId){
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
