package cn.partytime.collector.rpcService.alarmRpcService.impl;

import cn.partytime.collector.rpcService.alarmRpcService.DanmuService;
import org.springframework.stereotype.Service;

/**
 * Created by dm on 2017/7/20.
 */

@Service
public class DanmuServiceHystrix implements DanmuService {
    @Override
    public void danmuAlarm(String type, String code) {

    }
}
