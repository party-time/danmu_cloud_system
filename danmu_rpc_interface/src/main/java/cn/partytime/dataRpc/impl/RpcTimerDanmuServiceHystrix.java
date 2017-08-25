package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcTimerDanmuService;
import cn.partytime.model.TimerDanmuFileModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RpcTimerDanmuServiceHystrix implements RpcTimerDanmuService {
    @Override
    public List<TimerDanmuFileModel> findTimerDanmuFileList(List<String> partyIdList) {
        return null;
    }

    @Override
    public boolean findTimerDanmuIsExistAfterCurrentTime(long currentTime) {
        return true;
    }
}
