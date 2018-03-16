package cn.partytime.service;


import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.dataRpc.RpcTimerDanmuService;
import cn.partytime.model.PartyModel;
import cn.partytime.model.TimerDanmuFileModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimerDanmuLogicService {

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private RpcTimerDanmuService rpcTimerDanmuService;


    public List<TimerDanmuFileModel> findTimerDanmuFileList() {
        List<PartyModel> partyList = rpcPartyService.findByTypeAndStatus(1, 2);
        List<String> partyIdlList = new ArrayList<String>();
        if (ListUtils.checkListIsNotNull(partyList)) {
            partyList.stream().forEach(party -> partyIdlList.add(party.getId()));
            return rpcTimerDanmuService.findTimerDanmuFileList(partyIdlList);
        }
        return null;
    }
}
