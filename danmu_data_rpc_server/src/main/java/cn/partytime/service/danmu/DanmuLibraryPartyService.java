package cn.partytime.service.danmu;


import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.repository.danmu.DanmuLibraryPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by liuwei on 2016/10/21.
 */

@Controller
@RequestMapping("/danmuLibraryParty")
public class DanmuLibraryPartyService {

    @Autowired
    private DanmuLibraryPartyRepository danmuLibraryPartyRepository;

    public DanmuLibraryParty save(String danmuLibraryId, String partyId){
        DanmuLibraryParty danmuLibraryParty = danmuLibraryPartyRepository.findByPartyId(partyId);
        if( null == danmuLibraryParty) {
            danmuLibraryParty = new DanmuLibraryParty();
        }
        danmuLibraryParty.setPartyId(partyId);
        danmuLibraryParty.setDanmuLibraryId(danmuLibraryId);
        danmuLibraryPartyRepository.save(danmuLibraryParty);
        return danmuLibraryParty;
    }


    @RequestMapping(value = "/findByPartyId" ,method = RequestMethod.GET)
    public DanmuLibraryParty findByPartyId(@RequestParam String partyId){
        return danmuLibraryPartyRepository.findByPartyId(partyId);
    }

    public void delDanmuLibraryParty(String danmuLibraryId){
        List<DanmuLibraryParty> danmuLibraryPartyList =danmuLibraryPartyRepository.findByDanmuLibraryId(danmuLibraryId);
        if( null != danmuLibraryPartyList){
            for(DanmuLibraryParty danmuLibraryParty : danmuLibraryPartyList){
                danmuLibraryPartyRepository.delete(danmuLibraryParty);
            }
        }
    }


}
