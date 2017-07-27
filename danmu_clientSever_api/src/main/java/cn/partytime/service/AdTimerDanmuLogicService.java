package cn.partytime.service;

import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcAdTimerService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.AdTimerResource;
import cn.partytime.model.PartyModel;
import cn.partytime.model.TimerDanmuFileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdTimerDanmuLogicService {

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private RpcAdTimerService rpcAdTimerService;

    public AdTimerResource findTimerDanmuFileList(String addressId) {

        List<PartyModel> partyList = rpcPartyService.findByTypeAndStatus(1, 2);

        List<String> partyIdlList = new ArrayList<String>();
        if (ListUtils.checkListIsNotNull(partyList)) {
            partyList.stream().forEach(party -> partyIdlList.add(party.getId()));
        }

        return rpcAdTimerService.findTimerDanmuFileList(addressId,partyIdlList);
    }
}
