package cn.partytime.collector.dataService.impl;

import cn.partytime.collector.dataService.WechatUserInfoService;
import cn.partytime.collector.model.WechatUserInfo;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/5.
 */

@Component
public class WechatUserInfoServiceHystrix implements WechatUserInfoService {

    @Override
    public WechatUserInfo findByWechatId(String wechatId) {
        return null;
    }
}
