package cn.partytime.rpc;

import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.model.danmu.PreDanmuModel;
import cn.partytime.service.PreDanmuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dm on 2017/7/10.
 */

@RestController
@RequestMapping("/rpcRepDanmu")
public class RpcPreDanmuService {


    @Autowired
    private PreDanmuService preDanmuService;
    /**
     * 查找活动下的预制弹幕
     * @param partyId
     * @return
     */
    @RequestMapping(value = "/findByPartyId" ,method = RequestMethod.GET)
    public List<PreDanmuModel> findByPartyId(@RequestParam String partyId){
        return  preDanmuService.findByPartyId(partyId);
    }

}
