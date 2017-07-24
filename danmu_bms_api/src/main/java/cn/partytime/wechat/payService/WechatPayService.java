package cn.partytime.wechat.payService;

import cn.partytime.service.BmsWechatUserService;
import cn.partytime.util.PartyTimeConfig;
import cn.partytime.util.WechatSignUtil;
import cn.partytime.util.WeixinUtil;
import cn.partytime.wechat.entity.ReceiveUnifiedOrderXmlEntity;
import cn.partytime.wechat.pojo.WxJsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 2016/10/12.
 */
@Service
public class WechatPayService {

    @Autowired
    public BmsWechatUserService bmsWechatUserService;

    @Autowired
    private PartyTimeConfig partyTimeConfig;

    public WxJsConfig createWxjsConfig(String url) {
        String jsTicket = WeixinUtil.getJsTicket(bmsWechatUserService.getAccessToken());
        WxJsConfig wxJsConfig = WechatSignUtil.jsSignature(url, jsTicket);
        return wxJsConfig;
    }

    public Map<String, String> createUnifiedorder(String nonceStr, String timestamp, String openId, String body, String detail, String attach, Integer total_fee, String spbillCreateIp) {
        ReceiveUnifiedOrderXmlEntity receiveUnifiedOrderXmlEntity = WeixinUtil.unifiedorder(nonceStr,body, detail, attach, total_fee, spbillCreateIp, openId);
        Map<String, String> map = new HashMap<>();
        String packageStr = "prepay_id=" + receiveUnifiedOrderXmlEntity.getPrepay_id();
        String signType = "MD5";

        String jsPayStr = "appId=" + partyTimeConfig.getAppId() + "&nonceStr=" + nonceStr +
                "&package=" + packageStr +
                "&signType=" + signType +
                "&timeStamp=" + timestamp;

        map.put("paySign", WechatSignUtil.createPaySign(jsPayStr));
        map.put("packageStr", packageStr);
        map.put("signType", signType);
        map.put("timeStamp",timestamp);
        map.put("nonceStr",nonceStr);
        return map;
    }
}
