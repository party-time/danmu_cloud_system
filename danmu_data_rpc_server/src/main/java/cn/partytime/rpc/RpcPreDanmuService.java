package cn.partytime.rpc;

import cn.partytime.common.cachekey.PreDanmuCacheKey;
import cn.partytime.common.util.ListUtils;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.model.danmu.PreDanmu;
import cn.partytime.repository.danmu.DanmuLibraryPartyRepository;
import cn.partytime.service.PreDanmuService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
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
    /**
     * 查找活动下的预制弹幕
     * @param partyId
     * @return
     */
    @RequestMapping(value = "/findByPartyId" ,method = RequestMethod.GET)
    public List<PreDanmu> findByPartyId(@RequestParam String partyId){
        log.info("获取活动:{}下预置弹幕",partyId);
        return  preDanmuService.findByPartyId(partyId);
    }

    /**
     * 查找活动下的预制弹幕
     * @param partyId
     * @return
     */
    @RequestMapping(value = "/findDanmuLibraryIdByParty" ,method = RequestMethod.GET)
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
    }



}
