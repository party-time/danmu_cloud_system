package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.rpcService.dataRpc.DanmuLibraryPartyService;
import cn.partytime.model.DanmuLibraryParty;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/6.
 */

@Component
public class DanmuLibraryPartyServiceHystrix implements DanmuLibraryPartyService {
    @Override
    public DanmuLibraryParty findByPartyId(String partyId) {
        return null;
    }
}
