package cn.partytime.rpcService.dataRpc;

import cn.partytime.common.util.ServerConst;
import cn.partytime.model.MovieSchedule;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = MovieScheduleService.class)
public interface MovieScheduleService {

    @RequestMapping(value = "/rpcMovieSchedule/findByPartyIdAndAddressId" ,method = RequestMethod.GET)
    public List<MovieSchedule> findByPartyIdAndAddressId(@RequestParam(value = "partyId")String partyId, @RequestParam(value = "addressId") String addressId);


    @RequestMapping(value = "/rpcMovieSchedule/insertMovieSchedule" ,method = RequestMethod.POST)
    public MovieSchedule insertMovieSchedule(MovieSchedule movieSchedule);

    @RequestMapping(value = "/rpcMovieSchedule/updateMovieSchedule" ,method = RequestMethod.POST)
    public MovieSchedule updateMovieSchedule(@RequestBody MovieSchedule movieSchedule);
}
