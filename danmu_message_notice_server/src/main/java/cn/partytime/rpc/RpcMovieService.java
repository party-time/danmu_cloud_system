package cn.partytime.rpc;

import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.logicService.CommonDataService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.service.MovieTimeAlaramService;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.model.PartyModel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */
@RestController
@RequestMapping("/rpcMovie")
@Slf4j
public class RpcMovieService {
    @Autowired
    private MovieTimeAlaramService movieTimeAlaramService;

    @Autowired
    private MessageHandlerService messageHandlerService;

    @Autowired
    private RpcPartyService partyService;

    @Autowired
    private CommonDataService commonDataService;



    @RequestMapping(value = "/movieTime" ,method = RequestMethod.GET)
    public void movieTime(@RequestParam String partyId,@RequestParam String addressId, @RequestParam long time) {

        PartyModel party = partyService.getPartyByPartyId(partyId);

        MessageObject<Map<String,String>> mapMessageObject = null;
        Map<String,String> map = new HashMap<String,String>();
        if(party!=null && party.getMovieTime()!=0){
            long movieTime = party.getMovieTime();
            log.info("time:{},moviTime:{}",time,party.getMovieTime());
            if(time<movieTime){
                //触发事件过短
                map = commonDataService.setMapByAddressId(AlarmKeyConst.ALARM_KEY_MOVIESHORT,addressId,partyId);
                mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_SHORT,map);
            }else if(time >movieTime) {
                //触发时间过
                map = commonDataService.setMapByAddressId(AlarmKeyConst.ALARM_KEY_MOVIEOVERTIME,addressId,partyId);
                mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_SHORT,map);
            }else{
                return;
            }
        }else{
            long minute = time/1000/60;
            log.info("minute:{}",minute);
            if(minute<90){
                //触发事件过短
                map = commonDataService.setMapByAddressId(AlarmKeyConst.ALARM_KEY_MOVIESHORT,addressId,partyId);
                mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_SHORT,map);

            }else if(minute >180) {
                //触发时间过
                map = commonDataService.setMapByAddressId(AlarmKeyConst.ALARM_KEY_MOVIEOVERTIME,addressId,partyId);
                mapMessageObject = new MessageObject<Map<String,String>>(LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_SHORT,map);

            }

        }
        mapMessageObject.setValue(0);
        mapMessageObject.setThreshold(0);
        sendMessage(mapMessageObject);
    }

    private void sendMessage(MessageObject<Map<String,String>> map){
        messageHandlerService.messageHandler(movieTimeAlaramService,map);
    }
}
