package cn.partytime.check.rpcService.impl;

import cn.partytime.check.rpcService.DanmuLibraryPartyService;
import cn.partytime.check.model.DanmuLibraryParty;
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
