package cn.partytime.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by liuwei on 2016/10/31.
 */
@Data
@Component
public class PartyTimeConfig {

    @Value("${partyTime.url}")
    private String url;

    @Value("${appId}")
    private String appId;

    @Value("${appSecret}")
    private String appSecret;

    @Value("${appKey}")
    private String appKey;

    @Value("${mini_appId}")
    private String mini_appId;

    @Value("${mini_appSecret}")
    private String mini_appSecret;


    private String appId1;


    private String appKey1;


    private String appSecret1;

}
