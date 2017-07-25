package cn.partytime.controller;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.service.BmsMovieScheduleService;
import cn.partytime.service.MovieScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/1/16.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/movieSchedule")
@Slf4j
public class MovieScheduleController {

    @Autowired
    private MovieScheduleService movieScheduleService;

    @Autowired
    private BmsMovieScheduleService bmsMovieScheduleService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResultModel getPage(String partyId, String addressId, Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber -1;
        return bmsMovieScheduleService.findByPartyIdAndAddressId(partyId,addressId,pageNumber,pageSize);
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        movieScheduleService.del(id);
        return restResultModel;
    }


}
