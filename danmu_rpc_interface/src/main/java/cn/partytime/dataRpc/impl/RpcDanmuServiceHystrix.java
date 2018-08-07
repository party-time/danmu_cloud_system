package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcDanmuService;
import cn.partytime.model.DanmuLogModel;
import cn.partytime.model.DanmuModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RpcDanmuServiceHystrix implements RpcDanmuService {

    @Override
    public void setSendUserTime(String danmuId) {

    }

    @Override
    public DanmuModel setAdminAccepetTime(String danmuLogId) {
        return null;
    }

    @Override
    public void updateDanmuStatus(String id, int sendStatus) {
    }

    @Override
    public DanmuLogModel save(DanmuLogModel danmuLogModel) {
        return null;
    }

    @Override
    public DanmuLogModel findDanmuLogById(String id) {
        return null;
    }

    @Override
    public DanmuModel save(DanmuModel danmuModel) {
        return null;
    }

    @Override
    public DanmuModel findById(String id) {
        return null;
    }

    @Override
    public List<DanmuModel> findDanmuByIsBlocked(int page, int size, boolean isBlocked) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findHistoryDanmu(String partyId, int count, String id) {
        return null;
    }

    @Override
    public List<String> findDanmuPoolIdListByPartyIdAndAddressId(String partyId, String addressId) {
        return null;
    }

    @Override
    public long countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlgAndTimeGreaterThan(List<String> danmuPoolIdList, int danmuSrc, boolean isBlocked, long time) {
        return 0;
    }


}
