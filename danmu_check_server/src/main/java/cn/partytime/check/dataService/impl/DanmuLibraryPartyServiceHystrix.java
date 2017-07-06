package cn.partytime.check.dataService.impl;

import cn.partytime.check.dataService.DanmuLibraryPartyService;
import cn.partytime.check.model.DanmuLibraryParty;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
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
