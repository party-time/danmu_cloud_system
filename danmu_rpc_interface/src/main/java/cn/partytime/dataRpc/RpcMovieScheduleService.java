package cn.partytime.dataRpc;


import cn.partytime.dataRpc.impl.RpcMovieScheduleServiceHystrix;
import cn.partytime.model.MovieScheduleModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "${dataRpcServer}",fallback = RpcMovieScheduleServiceHystrix.class)
public interface RpcMovieScheduleService {

    @RequestMapping(value = "/rpcMovieSchedule/findByPartyIdAndAddressId" ,method = RequestMethod.GET)
    public List<MovieScheduleModel> findByPartyIdAndAddressId(@RequestParam(value = "partyId") String partyId, @RequestParam(value = "addressId") String addressId);


    @RequestMapping(value = "/rpcMovieSchedule/insertMovieSchedule" ,method = RequestMethod.POST)
    public MovieScheduleModel insertMovieSchedule(MovieScheduleModel movieScheduleModel);

    @RequestMapping(value = "/rpcMovieSchedule/updateMovieSchedule" ,method = RequestMethod.POST)
    public MovieScheduleModel updateMovieSchedule(@RequestBody MovieScheduleModel movieScheduleModel);
}
