package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.model.MovieSchedule;
import cn.partytime.rpcService.dataRpc.MovieScheduleService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieScheduleServiceHystrix implements MovieScheduleService {
    @Override
    public List<MovieSchedule> findByPartyIdAndAddressId(String partyId, String addressId) {
        return null;
    }

    @Override
    public MovieSchedule insertMovieSchedule(MovieSchedule movieSchedule) {
        return null;
    }

    @Override
    public MovieSchedule updateMovieSchedule(MovieSchedule movieSchedule) {
        return null;
    }
}
