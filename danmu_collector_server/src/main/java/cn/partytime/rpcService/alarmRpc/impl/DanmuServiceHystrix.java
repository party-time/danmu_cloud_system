package cn.partytime.rpcService.alarmRpc.impl;

import cn.partytime.rpcService.alarmRpc.DanmuService;
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
