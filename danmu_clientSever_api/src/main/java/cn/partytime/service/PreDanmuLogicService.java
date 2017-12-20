package cn.partytime.service;

import cn.partytime.common.cachekey.danmu.PreDanmuCacheKey;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcCmdService;
import cn.partytime.dataRpc.RpcPreDanmuService;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.CmdTempComponentData;
import cn.partytime.model.PreDanmuModel;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/12.
 */

@Service
@Slf4j
public class PreDanmuLogicService {

    @Autowired
    private RpcPreDanmuService rpcPreDanmuService;

    public void danmuListenHandler(String partyId,String addressId) {
        //rpcPreDanmuService.checkIsReInitPreDanmuIntoCache(partyId,addressId);
        log.info("------------------重新加载预制弹幕-------------------------");
        rpcPreDanmuService.reInitPreDanmuIntoCache(partyId,addressId);
    }
}
