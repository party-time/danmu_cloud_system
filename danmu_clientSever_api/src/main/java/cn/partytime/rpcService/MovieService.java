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


    @RequestMapping(value = "/rpcMovie/partyStatus" ,method = RequestMethod.GET)
    public RestResultModel partyStatus(@RequestParam(value = "registCode") String registCode);


    @RequestMapping(value = "/rpcMovie/partyStart" ,method = RequestMethod.GET)
    public RestResultModel partyStart(@RequestParam(value = "registCode") String registCode,@RequestParam(value = "command") String command, @RequestParam(value = "clientTime") long clientTime);


    @RequestMapping(value = "/rpcMovie/moviceStart" ,method = RequestMethod.GET)
    public RestResultModel moviceStart(@RequestParam(value = "partyId") String partyId,@RequestParam(value = "registCode") String registCode,@RequestParam(value = "clientTime") long clientTime);


    @RequestMapping(value = "/rpcMovie/moviceStop" ,method = RequestMethod.GET)
    public RestResultModel moviceStop(@RequestParam(value = "partyId") String partyId, @RequestParam(value = "registCode") String registCode,@RequestParam(value = "clientTime") long clientTime);
}
