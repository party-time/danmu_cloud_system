package cn.partytime.service;

import cn.partytime.model.*;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.model.manager.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwei on 2016/9/12.
 */
@Service
public class PartyResourceResultService {

    @Autowired
    private PartyService partyService;

    @Autowired
    private ResourceFileService resourceFileService;

    /**
     * 查询所有未下线的电影
     * @return
     */
    public List<PartyResourceResult> findLatelyParty(){
        List<PartyResourceResult> partyResourceResultList = new ArrayList<>();
        List<Party> partyList = partyService.findByTypeAndStatus(1,1);
        if( null != partyList && partyList.size() > 0){
            List<String> partyIds = new ArrayList<>();
            for(Party party : partyList){
                partyIds.add(party.getId());
            }
            Map<String,List<ResourceFile>>  resourceFileMap = resourceFileService.findByPartyIds(partyIds);
            for(Party party : partyList){
                PartyResourceResult partyResourceResult = new PartyResourceResult();
                partyResourceResult.setParty(party);
                if( null != resourceFileMap) {
                    partyResourceResult.setResourceFileList(resourceFileMap.get(party.getId()));
                }
                partyResourceResultList.add(partyResourceResult);
            }
        }
        return partyResourceResultList;
    }

    /**
     * 查询该场地下所有的未结束的活动
     * @param addressId
     * @param type
     * @return
     */
    public List<PartyResourceResult> findLatelyParty(String addressId,Integer type){
        List<PartyResourceResult> partyResourceResultList = new ArrayList<>();
        List<Party> partyList = partyService.findByType(addressId,type);
        if( null != partyList && partyList.size() > 0){
            List<String> partyIds = new ArrayList<>();
            for(Party party : partyList){
                partyIds.add(party.getId());
            }
            Map<String,List<ResourceFile>>  resourceFileMap = resourceFileService.findByPartyIds(partyIds);
            for(Party party : partyList){
                PartyResourceResult partyResourceResult = new PartyResourceResult();
                partyResourceResult.setParty(party);
                if( null != resourceFileMap) {
                    partyResourceResult.setResourceFileList(resourceFileMap.get(party.getId()));
                }
                partyResourceResultList.add(partyResourceResult);
            }
        }
        return partyResourceResultList;
    }

    public ResourceConfigJson findResourceConfig(Date startTime, Date endTime){
        ResourceConfigJson resourceConfigJson = new ResourceConfigJson();
        List<PartyResourceJson> partyResourceJsonList = new ArrayList<>();
        List<Party> partyList = partyService.findByStartTimeBetween(startTime,endTime);
        if( null != partyList && partyList.size() > 0){
            List<String> partyIds = new ArrayList<>();
            for(Party party : partyList){
                partyIds.add(party.getId());
            }
            Map<String,List<ResourceFile>>  resourceFileMap = resourceFileService.findByPartyIds(partyIds);
            for(Party party : partyList){
                PartyResourceJson partyResourceJson = new PartyResourceJson();
                partyResourceJson.setId(party.getId());
                partyResourceJson.setName(party.getName());
                partyResourceJson.setStartDate(party.getStartTime());
                partyResourceJson.setEndDate(party.getEndTime());
                if( null != resourceFileMap) {
                    List<ResourceFile> resourceFileList = resourceFileMap.get(party.getId());
                    if( null != resourceFileList && resourceFileList.size() > 0){
                        for(ResourceFile resourceFile : resourceFileList){

                        }
                    }
                }

            }
        }
        return resourceConfigJson;
    }

    


}
