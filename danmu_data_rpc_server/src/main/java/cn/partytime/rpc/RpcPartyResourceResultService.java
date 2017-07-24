package cn.partytime.rpc;

import cn.partytime.model.PartyResourceResult;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.service.PartyService;
import cn.partytime.service.ResourceFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/11.
 */
@RestController
@RequestMapping("/rpcPartyResourceResult")
public class RpcPartyResourceResultService {


    @Autowired
    private PartyService partyService;

    @Autowired
    private ResourceFileService resourceFileService;


    /**
     * 查询所有未下线的电影
     * @return
     */

    @RequestMapping(value = "/findLatelyParty" ,method = RequestMethod.POST)
    public List<PartyResourceResult> findLatelyParty(){
        List<PartyResourceResult> partyResourceResultList = new ArrayList<>();
        List<Party> partyList = partyService.findByTypeAndStatus(1,1);
        if( null != partyList && partyList.size() > 0){
            List<String> partyIds = new ArrayList<>();
            for(Party party : partyList){
                partyIds.add(party.getId());
            }
            Map<String,List<ResourceFile>> resourceFileMap = resourceFileService.findByPartyIds(partyIds);
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

    @RequestMapping(value = "/findLatelyPartyByAddressIdAndType" ,method = RequestMethod.POST)
    public List<PartyResourceResult> findLatelyPartyByAddressIdAndType(@RequestParam String addressId, @RequestParam Integer type){
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
}
