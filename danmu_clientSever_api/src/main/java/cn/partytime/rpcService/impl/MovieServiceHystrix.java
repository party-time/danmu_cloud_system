package cn.partytime.rpcService.impl;

import cn.partytime.rpcService.MovieService;
import cn.partytime.model.RestResultModel;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/12.
 */

@Component
public class MovieServiceHystrix implements MovieService {
    @Override
    public RestResultModel partyStatus(String registCode) {
        return null;
    }

    @Override
    public RestResultModel partyStart(String registCode, String command, long clientTime) {
        return null;
    }

    @Override
    public RestResultModel moviceStart(String partyId, String registCode, long clientTime) {
        return null;
    }

    @Override
    public RestResultModel moviceStop(String partyId, String registCode, long clientTime) {
        return null;
    }
}
