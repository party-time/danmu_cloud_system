package cn.partytime.rpc;

import cn.partytime.common.util.ListUtils;
import cn.partytime.model.TimerDanmuFileLogicModel;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.TimerDanmuFile;
import cn.partytime.service.PartyService;
import cn.partytime.service.TimerDanmuFileService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@RestController
@RequestMapping("/rpcTimerDanmu")
public class RpcTimerDanmuService {


    @Autowired
    private PartyService partyService;

    @Autowired
    private TimerDanmuFileService timerDanmuFileService;


    @RequestMapping(value = "/findTimerDanmuFileList" ,method = RequestMethod.GET)
    public List<TimerDanmuFileLogicModel> findTimerDanmuFileList(@RequestParam String addressId) {
        List<Party> partyList = partyService.findByTypeAndStatus(1, 2);
        List<String> partyIdlList = new ArrayList<String>();
        if (ListUtils.checkListIsNotNull(partyList)) {
            partyList.stream().forEach(party -> partyIdlList.add(party.getId()));
        }
        List<TimerDanmuFile> timerDanmuFileList = timerDanmuFileService.findByPartyId(partyIdlList);
        List<TimerDanmuFileLogicModel> timerDanmuFileLogicModelList = new ArrayList<TimerDanmuFileLogicModel>();
        if (ListUtils.checkListIsNotNull(timerDanmuFileList)) {
            for (TimerDanmuFile timerDanmuFile : timerDanmuFileList) {
                TimerDanmuFileLogicModel timerDanmuFileLogicModel = new TimerDanmuFileLogicModel();
                BeanUtils.copyProperties(timerDanmuFile, timerDanmuFileLogicModel);
                timerDanmuFileLogicModelList.add(timerDanmuFileLogicModel);
            }
        }
        return timerDanmuFileLogicModelList;
    }

}
