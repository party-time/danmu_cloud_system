package cn.partytime.rpc;


import cn.partytime.common.util.DateUtils;
import cn.partytime.logicService.PartyLogicService;
import cn.partytime.model.MovieScheduleModel;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.manager.MovieSchedule;
import cn.partytime.service.MovieScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rpcMovieSchedule")
public class RpcMovieScheduleService {

    @Autowired
    private MovieScheduleService movieScheduleService;

    @Autowired
    private PartyLogicService partyLogicService;


    @RequestMapping(value = "/findByCurrentMovieLastTime" ,method = RequestMethod.GET)
    public long findByCurrentMovieLastTime(@RequestParam String partyId, @RequestParam String addressId){
        Page<MovieSchedule> movieSchedulePage = movieScheduleService.findPageByPartyIdAndAddressIsAndCreateTimeAsc(partyId,addressId,1,0);
        if(movieSchedulePage.getTotalElements()==0){
            return 0;
        }
        MovieSchedule movieSchedule = movieSchedulePage.getContent().get(0);
        Date startDate = movieSchedule.getStartTime();
        Date endDate = movieSchedule.getEndTime()==null? DateUtils.addMinuteToDate(startDate,180):movieSchedule.getEndTime();
        long movieTime = endDate.getTime() - startDate.getTime();

        PartyLogicModel partyLogicModel = partyLogicService.findFilmParty(addressId);
        Date partyStartDate = partyLogicModel.getStartTime();
        Date currentDate = DateUtils.getCurrentDate();
        long subTime = currentDate.getTime() - partyStartDate.getTime();

        return Math.abs(movieTime - subTime);
    }

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

    @RequestMapping(value = "/findCurrentMovie" ,method = RequestMethod.GET)
    public MovieSchedule findCurrentMovie(String partyId,String addressId){
        Page<MovieSchedule> movieSchedulePage = movieScheduleService.findPageByPartyIdAndAddressIs(partyId,addressId,1,0);
        if(movieSchedulePage.getTotalElements()==0){
            return null;
        }
        return movieSchedulePage.getContent().get(0);
    }

}
