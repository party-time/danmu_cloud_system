package cn.partytime.rpcService.impl;

import cn.partytime.rpcService.TimerService;
import cn.partytime.model.TimerDanmuFileLogicModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@Component
public class TimerServiceHystrix implements TimerService {
    @Override
    public List<TimerDanmuFileLogicModel> findTimerDanmuFileList(String addressId) {
        return null;
    }
}
