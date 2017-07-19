package cn.partytime.rpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.rpcService.impl.MovieServiceHystrix;
import cn.partytime.model.RestResultModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/12.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = MovieServiceHystrix.class)
public interface MovieService {



    @RequestMapping(value = "/rpcMovie/partyStart" ,method = RequestMethod.GET)
    public RestResultModel partyStart(@RequestParam(value = "partyId") String partyId,@RequestParam(value = "addressId") String addressId, @RequestParam(value = "clientTime") long clientTime);


    @RequestMapping(value = "/rpcMovie/moviceStart" ,method = RequestMethod.GET)
    public RestResultModel movieStart(@RequestParam(value = "partyId") String partyId,@RequestParam(value = "addressId") String addressId,@RequestParam(value = "clientTime") long clientTime);


    @RequestMapping(value = "/rpcMovie/moviceStop" ,method = RequestMethod.GET)
    public RestResultModel movieStop(@RequestParam(value = "partyId") String partyId, @RequestParam(value = "addressId") String addressId,@RequestParam(value = "clientTime") long clientTime);
}
