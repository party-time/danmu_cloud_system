package cn.partytime.service;

import cn.partytime.common.util.ListUtils;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.danmu.AdDanmuLibrary;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.PartyAddressAdRelation;
import cn.partytime.service.adDanmu.AdDanmuLibraryService;
import cn.partytime.service.adDanmu.PartyAddressAdRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lENOVO on 2016/9/27.
 */

@Service
public class BmsAddressService {


    @Autowired
    private DanmuAddressService danmuAddressService;


    @Autowired
    private PartyAddressRelationService partyAddressRelationService;

    @Autowired
    private BmsPartyAddressAdRelationService bmsPartyAddressAdRelationService;

    @Autowired
    private AdDanmuLibraryService adDanmuLibraryService;

    @Autowired
    private PartyAddressAdRelationService partyAddressAdRelationService;

    /**
     * 验证 地点不存在
     *
     * @param longitude
     * @param latitude
     * @return
     */
    public boolean checkAddressIsNotExist(double longitude, double latitude,int range) {
        List<String> addressList = danmuAddressService.findAddressIdList(longitude, latitude, range);
        if (addressList == null || addressList.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @param longitude
     * @param latitude
     * @param addressId
     * @return
     */
    public boolean checkAddressIsExist(double longitude, double latitude, String addressId,int range) {
        List<String> addressList = danmuAddressService.findAddressIdList(longitude, latitude, range);
        if (addressList == null || addressList.isEmpty()) {
            return true;
        }
        if (addressList.size() > 1) {
            return false;
        }
        if (addressId.equals(addressList.get(0))) {
            return true;
        }
        return false;
    }



    public PageResultModel findAddressByPartyId(String partyId, Integer pageNumber, Integer pageSize){
        PageResultModel pageResultModel = new PageResultModel();
        List<String> addressList =  danmuAddressService.findAddress(partyId);
        if(!ListUtils.checkListIsNotNull(addressList)){
            //活动下没有场地
            pageResultModel.setTotal(0);
            pageResultModel.setRows(null);
            return pageResultModel;
        }
        Page<DanmuAddress> danmuAddressPage = danmuAddressService.findAddressByPartyId(addressList,pageNumber,pageSize);
        List<String> relationAdIdList = new ArrayList<String>();
        List<PartyAddressAdRelation> partyAddressAdRelationList = partyAddressAdRelationService.findPartyAddressAdRelationByPartyIdAndAddressIdList(partyId,addressList);
        Map<String,String> addressPoolMap = new HashMap<String,String>();
        Map<String,String> poolMap = new HashMap<String,String>();
        if(ListUtils.checkListIsNotNull(partyAddressAdRelationList)){
            for(PartyAddressAdRelation partyAddressAdRelation:partyAddressAdRelationList){
                addressPoolMap.put(partyAddressAdRelation.getAddressId(),partyAddressAdRelation.getAdDanmuPoolId());
                relationAdIdList.add(partyAddressAdRelation.getAdDanmuPoolId());
            }
            List<AdDanmuLibrary>  danmuLibraryList =  adDanmuLibraryService.findAdDanmuLibraryByIdList(relationAdIdList);
            if(ListUtils.checkListIsNotNull(danmuLibraryList)){
                for(AdDanmuLibrary adDanmuLibrary:danmuLibraryList){
                    poolMap.put(adDanmuLibrary.getId(),adDanmuLibrary.getName());
                }
            }
        }

        long count  = danmuAddressPage.getTotalElements();
        pageResultModel.setTotal(count);
        List<DanmuAddress> danmuAddressList = danmuAddressPage.getContent();
        List<DanmuAddressModel> danmuAddressModelList = null;
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            danmuAddressModelList = new ArrayList<DanmuAddressModel>();
            for(DanmuAddress danmuAddress:danmuAddressList){
                DanmuAddressModel danmuAddressModel = new DanmuAddressModel();
                BeanUtils.copyProperties(danmuAddress,danmuAddressModel);
                String poolId = addressPoolMap.get(danmuAddress.getId());
                String name = poolMap.get(poolId);
                danmuAddressModel.setAdName(name);
                danmuAddressModelList.add(danmuAddressModel);
            }
            pageResultModel.setRows(danmuAddressModelList);
        }
        return pageResultModel;
    }

    public PageResultModel findAdByPartyId(String partyId, Integer pageNumber, Integer pageSize){
        PageResultModel  pageResultModel = new PageResultModel();
        List<DanmuAddress> addressList =  danmuAddressService.findByType(0);
        if(!ListUtils.checkListIsNotNull(addressList)){
            //活动下没有场地
            pageResultModel.setTotal(0);
            pageResultModel.setRows(null);
            return pageResultModel;
        }

        List<String> addressIdList = new ArrayList<>();
        for(DanmuAddress dmAddress : addressList){
            addressIdList.add(dmAddress.getId());
        }

        Page<DanmuAddress> danmuAddressPage = danmuAddressService.findAddressByPartyId(addressIdList,pageNumber,pageSize);
        List<String> relationAdIdList = new ArrayList<String>();
        List<PartyAddressAdRelation> partyAddressAdRelationList = partyAddressAdRelationService.findPartyAddressAdRelationByPartyIdAndAddressIdList(partyId,addressIdList);
        Map<String,String> addressPoolMap = new HashMap<String,String>();
        Map<String,String> poolMap = new HashMap<String,String>();
        if(ListUtils.checkListIsNotNull(partyAddressAdRelationList)){
            for(PartyAddressAdRelation partyAddressAdRelation:partyAddressAdRelationList){
                addressPoolMap.put(partyAddressAdRelation.getAddressId(),partyAddressAdRelation.getAdDanmuPoolId());
                relationAdIdList.add(partyAddressAdRelation.getAdDanmuPoolId());
            }
            List<AdDanmuLibrary>  danmuLibraryList =  adDanmuLibraryService.findAdDanmuLibraryByIdList(relationAdIdList);
            if(ListUtils.checkListIsNotNull(danmuLibraryList)){
                for(AdDanmuLibrary adDanmuLibrary:danmuLibraryList){
                    poolMap.put(adDanmuLibrary.getId(),adDanmuLibrary.getName());
                }
            }
        }

        long count  = danmuAddressPage.getTotalElements();
        pageResultModel.setTotal(count);
        List<DanmuAddress> danmuAddressList = danmuAddressPage.getContent();
        List<DanmuAddressModel> danmuAddressModelList = null;
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            danmuAddressModelList = new ArrayList<DanmuAddressModel>();
            for(DanmuAddress danmuAddress:danmuAddressList){
                DanmuAddressModel danmuAddressModel = new DanmuAddressModel();
                BeanUtils.copyProperties(danmuAddress,danmuAddressModel);
                String poolId = addressPoolMap.get(danmuAddress.getId());
                String name = poolMap.get(poolId);
                danmuAddressModel.setAdName(name);
                danmuAddressModelList.add(danmuAddressModel);
            }
            pageResultModel.setRows(danmuAddressModelList);
        }
        return pageResultModel;
    }
}
