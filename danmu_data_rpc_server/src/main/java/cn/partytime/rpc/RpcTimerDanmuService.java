package cn.partytime.rpc;

import cn.partytime.model.manager.TimerDanmuFile;
import cn.partytime.service.PartyService;
import cn.partytime.service.TimerDanmuFileService;
import cn.partytime.service.TimerDanmuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@RestController
@RequestMapping("/rpcTimerDanmu")
public class RpcTimerDanmuService {

    @Autowired
    private TimerDanmuService timerDanmuService;

    @Autowired
    private TimerDanmuFileService timerDanmuFileService;


    @RequestMapping(value = "/findTimerDanmuFileList" ,method = RequestMethod.GET)
    public List<TimerDanmuFile> findTimerDanmuFileList(@RequestParam List<String> partyIdList) {
        List<TimerDanmuFile> timerDanmuFileList = timerDanmuFileService.findByPartyId(partyIdList);
        return timerDanmuFileList;
    }

    @RequestMapping(value = "/findTimerDanmuIsExistAfterCurrentTime" ,method = RequestMethod.GET)
    public boolean findTimerDanmuIsExistAfterCurrentTime(@RequestParam long time){
        long count =  timerDanmuService.countByPartyIdLessThanBAndBeginTime(time);
        if(count>0){
            return true;
        }
        return false;
    }

}
