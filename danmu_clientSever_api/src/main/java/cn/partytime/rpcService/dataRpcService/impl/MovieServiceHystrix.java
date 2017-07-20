package cn.partytime.rpcService.dataRpcService.impl;

import cn.partytime.rpcService.dataRpcService.MovieService;
import cn.partytime.model.RestResultModel;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/12.
 */

@Component
public class MovieServiceHystrix implements MovieService {


    @Override
    public RestResultModel partyStart(String partyId, String addressId, long clientTime) {
        return null;
    }

    @Override
    public RestResultModel movieStart(String partyId, String registCode, long clientTime) {
        return null;
    }

    @Override
    public RestResultModel movieStop(String partyId, String registCode, long clientTime) {
        return null;
    }

}
