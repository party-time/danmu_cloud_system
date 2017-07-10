package cn.partytime.rpc;

import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.service.DanmuLibraryPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dm on 2017/7/10.
 */

@RestController
@RequestMapping("/rpcDanmuLibraryParty")
public class RpcDanmuLibraryPartyService {


    @Autowired
    private DanmuLibraryPartyService danmuLibraryPartyService;

    @RequestMapping(value = "/findByPartyId" ,method = RequestMethod.GET)
    public DanmuLibraryParty findByPartyId(@RequestParam String partyId){
        return danmuLibraryPartyService.findByPartyId(partyId);
    }

}
