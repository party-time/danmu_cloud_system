package cn.partytime.dataRpc;


import cn.partytime.dataRpc.impl.RpcMovieScheduleServiceHystrix;
import cn.partytime.model.MovieScheduleModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(value = "${dataRpcServer}",fallback = RpcMovieScheduleServiceHystrix.class)
public interface RpcMovieScheduleService {

    @RequestMapping(value = "/rpcMovieSchedule/findByCurrentMovieLastTime" ,method = RequestMethod.GET)
    public long findByCurrentMovieLastTime(@RequestParam (value = "partyId") String partyId, @RequestParam(value = "addressId") String addressId);


    @RequestMapping(value = "/rpcMovieSchedule/findByCurrentMoviePastTime" ,method = RequestMethod.GET)
    public long findByCurrentMoviePastTime(@RequestParam (value = "partyId") String partyId, @RequestParam(value = "addressId") String addressId);



    @RequestMapping(value = "/rpcMovieSchedule/findByPartyIdAndAddressId" ,method = RequestMethod.GET)
    public List<MovieScheduleModel> findByPartyIdAndAddressId(@RequestParam(value = "partyId") String partyId, @RequestParam(value = "addressId") String addressId);


    @RequestMapping(value = "/rpcMovieSchedule/insertMovieSchedule" ,method = RequestMethod.POST)
    public MovieScheduleModel insertMovieSchedule(MovieScheduleModel movieScheduleModel);

    @RequestMapping(value = "/rpcMovieSchedule/updateMovieSchedule" ,method = RequestMethod.POST)
    public MovieScheduleModel updateMovieSchedule(@RequestBody MovieScheduleModel movieScheduleModel);

    @RequestMapping(value = "/rpcMovieSchedule/findCurrentMovie" ,method = RequestMethod.GET)
    public MovieScheduleModel findCurrentMovie(@RequestParam(value = "partyId") String partyId, @RequestParam(value = "addressId") String addressId);


    @RequestMapping(value = "/rpcMovieSchedule/findLastMovieByAddressId" ,method = RequestMethod.GET)
    public MovieScheduleModel findLastMovieByAddressId(@RequestParam(value = "addressId") String addressId);

    @RequestMapping(value = "/rpcMovieSchedule/countByCreateTimeGreaterThanSeven" ,method = RequestMethod.GET)
    public long countByCreateTimeGreaterThanSeven();


    @RequestMapping(value = "/rpcMovieSchedule/findLastMovieListByAddressId" ,method = RequestMethod.GET)
    public List<MovieScheduleModel> findLastMovieListByAddressId(@RequestParam(value = "addressId") String addressId,@RequestParam(value = "pageSize") long pageSize,@RequestParam(value = "pageNo") long pageNo);
}
