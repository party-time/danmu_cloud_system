package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcPayDanmuService;
import cn.partytime.model.DanmuLogModel;
import cn.partytime.model.PayDanmuDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by admin on 2018/6/7.
 */

@Component
@Slf4j
public class RpcPayDanmuServiceHystrix implements RpcPayDanmuService {

    @Override
    public PayDanmuDto findDanmuLogById(String id) {
        return null;
    }

    @Override
    public void save(PayDanmuDto payDanmuDto) {

    }


}
