package cn.partytime.service.wechat;

import cn.partytime.model.wechat.WechatUserLog;
import cn.partytime.repository.user.WechatUserLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by administrator on 2016/11/30.
 */
@Service
public class WechatUserLogService {

    @Autowired
    private WechatUserLogRepository wechatUserLogRepository;

    public void save(String openId,String log){
        WechatUserLog wechatUserLog = new WechatUserLog();
        wechatUserLog.setOpenId(openId);
        wechatUserLog.setLog(log);
        wechatUserLogRepository.save(wechatUserLog);
    }
}
