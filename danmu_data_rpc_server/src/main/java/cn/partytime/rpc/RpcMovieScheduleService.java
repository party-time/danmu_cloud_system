package cn.partytime.rpc;


import cn.partytime.model.manager.MovieSchedule;
import cn.partytime.service.MovieScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rpcMovieSchedule")
public class RpcMovieScheduleService {

    @Autowired
    private MovieScheduleService movieScheduleService;

    @RequestMapping(value = "/findByPartyIdAndAddressId" ,method = RequestMethod.GET)
    public List<MovieSchedule> findByPartyIdAndAddressId(@RequestParam String partyId, @RequestParam String addressId){
        return movieScheduleService.findByPartyIdAndAddressId(partyId,addressId);
    }

    @RequestMapping(value = "/insertMovieSchedule" ,method = RequestMethod.POST)
    public MovieSchedule insertMovieSchedule(@RequestBody MovieSchedule movieSchedule){
       return movieScheduleService.insertMovieSchedule(movieSchedule);
    }

    @RequestMapping(value = "/updateMovieSchedule" ,method = RequestMethod.POST)
    public MovieSchedule updateMovieSchedule(@RequestBody MovieSchedule movieSchedule){
       return movieScheduleService.updateMovieSchedule(movieSchedule);
    }
}
