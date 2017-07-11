package cn.partytime.service;

import cn.partytime.model.MovieScheduleResult;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.MovieSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by administrator on 2017/1/20.
 */
@Service
public class BmsMovieScheduleService {

    @Autowired
    private MovieScheduleService movieScheduleService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    public PageResultModel findByPartyIdAndAddressId(String partyId, String addressId, Integer pageNo, Integer pageSize){
        Page<MovieSchedule> movieSchedulePage = null;
        if(StringUtils.isEmpty(addressId) || "0".equals(addressId) || "-1".equals(addressId)){
            movieSchedulePage = movieScheduleService.findAll(partyId,pageSize,pageNo);
        }else{
            movieSchedulePage = movieScheduleService.findPageByPartyIdAndAddressIs(partyId,addressId,pageSize,pageNo);
        }
        if( null != movieSchedulePage){
            List<MovieScheduleResult> movieScheduleResultList = new ArrayList<>();
            Set<String> addressIdSet = new HashSet<>();

            for(MovieSchedule movieSchedule : movieSchedulePage.getContent()){
                MovieScheduleResult movieScheduleResult = new MovieScheduleResult();
                movieScheduleResult.setMovieSchedule(movieSchedule);
                addressIdSet.add(movieSchedule.getAddressId());
                movieScheduleResultList.add(movieScheduleResult);
            }

            List<DanmuAddress> danmuAddressList = danmuAddressService.findDanmuAddressByIdList(new ArrayList<>(addressIdSet));

            for(MovieScheduleResult movieScheduleResult : movieScheduleResultList){
                for(DanmuAddress danmuAddress : danmuAddressList){
                    if(movieScheduleResult.getMovieSchedule().getAddressId().equals(danmuAddress.getId())){
                        movieScheduleResult.setDanmuAddress(danmuAddress);
                    }
                }
            }

            PageResultModel pageResultModel = new PageResultModel();
            pageResultModel.setRows(movieScheduleResultList);
            pageResultModel.setTotal(movieSchedulePage.getTotalElements());
            return pageResultModel;
        }else{
            return null;
        }

    }
}
