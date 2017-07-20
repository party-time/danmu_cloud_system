package cn.partytime.rpcService.dataRpcService.impl;

import cn.partytime.rpcService.dataRpcService.PromoService;
import cn.partytime.model.RestResultModel;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/12.
 */
@Component
public class PromoServiceHystrix implements PromoService {
    @Override
    public RestResultModel sendPromoCommand(String name, String status, String registCode) {
        return null;
    }
}
