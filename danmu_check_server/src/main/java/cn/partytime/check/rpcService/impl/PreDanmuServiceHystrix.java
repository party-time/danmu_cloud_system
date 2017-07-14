package cn.partytime.check.rpcService.impl;

import cn.partytime.check.rpcService.PreDanmuService;
import cn.partytime.check.model.PreDanmuModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dm on 2017/7/6.
 */

@Component
public class PreDanmuServiceHystrix implements PreDanmuService {
    @Override
    public List<PreDanmuModel> findByPartyId(String partyId) {
        return null;
    }
}
