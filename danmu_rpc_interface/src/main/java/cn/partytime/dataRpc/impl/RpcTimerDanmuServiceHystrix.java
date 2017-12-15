package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcTimerDanmuService;
import cn.partytime.model.TimerDanmuFileModel;
import cn.partytime.model.TimerDanmuModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RpcTimerDanmuServiceHystrix implements RpcTimerDanmuService {
    @Override
    public List<TimerDanmuFileModel> findTimerDanmuFileList(List<String> partyIdList) {
        return null;
    }

    @Override
    public long countByPartyIdAndBeginTimeGreaterThan(String partyId, long time) {
        return 0;
    }

    @Override
    public List<TimerDanmuModel> findByPartyIdOrderBytimeDesc(String partyId, int pageSize, int pageNo) {
        return null;
    }


}
