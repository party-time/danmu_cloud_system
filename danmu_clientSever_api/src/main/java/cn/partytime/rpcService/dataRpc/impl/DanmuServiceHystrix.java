package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.rpcService.dataRpc.DanmuService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/11.
 */

@Component
public class DanmuServiceHystrix implements DanmuService {
    @Override
    public List<Map<String, Object>> findHistoryDanmu(String partyId, int time, int count) {
        return null;
    }
}
