package cn.partytime.rpc;

import cn.partytime.cache.danmu.PreDanmuCacheService;
import cn.partytime.cache.danmu.PreDanmuLibraryCacheService;
import cn.partytime.common.util.ListUtils;
import cn.partytime.logicService.PreDanmuLogicService;
import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.model.danmu.PreDanmu;
import cn.partytime.repository.danmu.DanmuLibraryPartyRepository;
import cn.partytime.service.DanmuLibraryPartyService;
import cn.partytime.service.PreDanmuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/10.
 */

@RestController
@RequestMapping("/rpcRepDanmu")
@Slf4j
public class RpcPreDanmuService {


    @Autowired
    private PreDanmuService preDanmuService;

    @Autowired
    private DanmuLibraryPartyRepository danmuLibraryPartyRepository;

    @Autowired
    private PreDanmuCacheService preDanmuCacheService;

    @Autowired
    private PreDanmuLogicService preDanmuLogicService;

    @Autowired
    private DanmuLibraryPartyService danmuLibraryPartyService;

    @Autowired
    private PreDanmuLibraryCacheService preDanmuLibraryCacheService;

    @RequestMapping(value = "/getPartyDanmuDensity" ,method = RequestMethod.GET)
    public int getPartyDanmuDensity(@RequestParam String partyId) {
        return preDanmuLogicService.getPartyDanmuDensity(partyId);
    }

    @RequestMapping(value = "/initPreDanmuIntoCache" ,method = RequestMethod.GET)
    public void initPreDanmuIntoCache(@RequestParam String partyId) {
        preDanmuLogicService.initPreDanmu(partyId);
    }

    @RequestMapping(value = "/getPreDanmuFromCache" ,method = RequestMethod.GET)
    public Map<String,Object> getPreDanmuFromCache(@RequestParam String partyId, @RequestParam int danmuCount) {
        log.info("获取活动:{},弹幕级别:{}",partyId,danmuCount);
        return preDanmuLogicService.getPreDanmuFromCache(partyId,danmuCount);
    }

    @RequestMapping(value = "/setPreDanmuLibrarySortRule" ,method = RequestMethod.GET)
    public void setPreDanmuLibrarySortRule(@RequestParam String partyId) {
        preDanmuLogicService.setPreDanmuLibrarySortRule(partyId);
    }
    @RequestMapping(value = "/removePreDanmuCache" ,method = RequestMethod.GET)
    public void removePreDanmuCache(@RequestParam String partyId) {

        List<DanmuLibraryParty> danmuLibraryPartyList =   danmuLibraryPartyService.findByPartyId(partyId);
        if(ListUtils.checkListIsNotNull(danmuLibraryPartyList)){
            danmuLibraryPartyList.forEach(danmuLibraryParty -> preDanmuCacheService.removePreDanmuFromCache(partyId,danmuLibraryParty.getDanmuLibraryId()));
        }

        preDanmuLibraryCacheService.removePreDanmuLibrary(partyId);
    }

    @RequestMapping(value = "/findDanmuLibraryIdByParty" ,method = RequestMethod.GET)
    public List<String> findDanmuLibraryIdByParty(@RequestParam String partyId) {
        log.info("获取活动的预置弹幕库编号");
        List<DanmuLibraryParty> danmuLibraryPartyList = danmuLibraryPartyRepository.findByPartyIdOrderByCreateTimeAsc(partyId);
        List<String> libraryIdList = new ArrayList<String>();
        if(ListUtils.checkListIsNotNull(danmuLibraryPartyList)){
            danmuLibraryPartyList.forEach(danmuLibraryParty->libraryIdList.add(danmuLibraryParty.getDanmuLibraryId()));
            return libraryIdList;
        }
        return null;
    }


    @RequestMapping(value = "/findPreDanmuByLibraryId" ,method = RequestMethod.GET)
    public List<PreDanmu> findPreDanmuByLibraryId(@RequestParam String libraryId,@RequestParam int page,@RequestParam int size) {
        Page<PreDanmu> preDanmuPage = preDanmuService.findPageByDLId(page,size,libraryId);
        return preDanmuPage.getContent();
    }

    @RequestMapping(value = "/findPreDanmuCountByLibraryId" ,method = RequestMethod.GET)
    public long findPreDanmuCountByLibraryId(@RequestParam String libraryId) {
        return  preDanmuService.countByDanmuLibraryId(libraryId);
    }

    /**
     * 查找活动下的预制弹幕
     * @param partyId
     * @return
     */
   /*@RequestMapping(value = "/findDanmuLibraryIdByParty" ,method = RequestMethod.GET)
    public String findDanmuLibraryIdByParty(@RequestParam String partyId) {
        DanmuLibraryParty danmuLibraryParty = danmuLibraryPartyRepository.findByPartyId(partyId);
        if(danmuLibraryParty!=null){
            return danmuLibraryParty.getDanmuLibraryId();
        }
        return "";
    }

    @RequestMapping(value = "/findDanmuLibraryIdByParty" ,method = RequestMethod.GET)
    public List<PreDanmu> findPreDanmuByLibraryId(@RequestParam String libraryId,@RequestParam int page,@RequestParam int size) {
        Page<PreDanmu> preDanmuPage = preDanmuService.findPageByDLId(page,size,libraryId);
        return preDanmuPage.getContent();
    }*/



}
