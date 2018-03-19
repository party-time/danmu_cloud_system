package cn.partytime.rpc;

import cn.partytime.cache.danmu.PreDanmuCacheService;
import cn.partytime.cache.danmu.PreDanmuLibraryCacheService;
import cn.partytime.common.util.ListUtils;
import cn.partytime.logicService.PreDanmuLogicService;
import cn.partytime.model.danmu.DanmuLibrary;
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






    @RequestMapping(value = "/updateDensityByPartyIdAndLiBraryIdAndDensity" ,method = RequestMethod.GET)
    public void updateDensityByPartyIdAndLiBraryIdAndDensity(@RequestParam String partyId,@RequestParam String libraryId,@RequestParam Integer density) {
        danmuLibraryPartyService.save(libraryId,partyId,density);
    }

    @RequestMapping(value = "/getPartyDanmuDensity" ,method = RequestMethod.GET)
    public int getPartyDanmuDensity(@RequestParam String partyId) {
        return preDanmuLogicService.getPartyDanmuDensity(partyId);
    }

    /**
     * 初始化弹幕队列
     * @param partyId
     */
    @RequestMapping(value = "/initPreDanmuIntoCache" ,method = RequestMethod.GET)
    public void initPreDanmuIntoCache(@RequestParam String partyId,@RequestParam String addressId) {
        log.info("================================初始化预置弹幕库");
        preDanmuLogicService.initPreDanmuIntoCache(partyId,addressId);
    }

    @RequestMapping(value = "/reInitPreDanmuIntoCache" ,method = RequestMethod.GET)
    public void reInitPreDanmuIntoCache(@RequestParam String partyId,@RequestParam String addressId) {
        log.info("---------------------------------重新对预置弹幕库进行加载");
        preDanmuLogicService.reInitPreDanmuIntoCache(partyId,addressId);
    }

    /**
     * 初始化弹幕队列
     * @param partyId
     */
    @RequestMapping(value = "/checkIsReInitPreDanmuIntoCache" ,method = RequestMethod.GET)
    public void checkIsReInitPreDanmuIntoCache(@RequestParam String partyId,@RequestParam String addressId) {
        log.info("++++++++++++++++++++++++++++++++电影开始的时候检测预置弹幕是否加载成功");
        preDanmuLogicService.initPreDanmuIntoCache(partyId,addressId);
    }

    @RequestMapping(value = "/getPreDanmuFromCache" ,method = RequestMethod.GET)
    public Map<String,Object> getPreDanmuFromCache(@RequestParam String partyId,@RequestParam String addressId, @RequestParam int danmuCount) {

        int density =  preDanmuLogicService.getPartyDanmuDensity(partyId);
        log.info("获取活动:{},弹幕级别:{}",partyId,danmuCount);
        return preDanmuLogicService.getPreDanmuFromCache(partyId,addressId,danmuCount);
    }

    @RequestMapping(value = "/setPreDanmuLibrarySortRule" ,method = RequestMethod.GET)
    public void setPreDanmuLibrarySortRule(@RequestParam String partyId) {
        preDanmuLogicService.setPreDanmuLibrarySortRule(partyId);
    }
    @RequestMapping(value = "/removePreDanmuCache" ,method = RequestMethod.GET)
    public void removePreDanmuCache(@RequestParam String partyId,@RequestParam String addressId) {

        List<DanmuLibraryParty> danmuLibraryPartyList =   danmuLibraryPartyService.findByPartyId(partyId);
        if(ListUtils.checkListIsNotNull(danmuLibraryPartyList)){
            danmuLibraryPartyList.forEach(danmuLibraryParty -> preDanmuCacheService.removePreDanmuFromCache(partyId,addressId,danmuLibraryParty.getDanmuLibraryId()));
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


    @RequestMapping(value = "/findPreDanmuLibraryListBylibraryIdList" ,method = RequestMethod.GET)
    public List<DanmuLibrary> findPreDanmuLibraryListBylibraryIdList(List<String> libraryIdList){
        return  preDanmuService.findByIdIn(libraryIdList);
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
