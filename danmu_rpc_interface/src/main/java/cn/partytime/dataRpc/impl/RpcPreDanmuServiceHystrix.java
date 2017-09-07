package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcPreDanmuService;
import cn.partytime.model.PreDanmuModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RpcPreDanmuServiceHystrix implements RpcPreDanmuService {
    @Override
    public List<PreDanmuModel> findByPartyId(String partyId) {
        log.info("获取预置弹幕为空");
        return null;
    }

    @Override
    public List<String> findDanmuLibraryIdByParty(String partyId) {
        return null;
    }

    @Override
    public List<PreDanmuModel> findPreDanmuByLibraryId(String libraryId, int page, int size) {
        return null;
    }

    @Override
    public long findPreDanmuCountByLibraryId(String libraryId) {
        return 0;
    }
}
