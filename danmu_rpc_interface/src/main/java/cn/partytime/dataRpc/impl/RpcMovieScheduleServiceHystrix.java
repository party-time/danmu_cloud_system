package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcMovieScheduleService;
import cn.partytime.model.MovieScheduleModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RpcMovieScheduleServiceHystrix implements RpcMovieScheduleService {

    @Override
    public long findByCurrentMovieTime(String partyId, String addressId) {
        return 0;
    }

    @Override
    public List<MovieScheduleModel> findByPartyIdAndAddressId(String partyId, String addressId) {
        return null;
    }

    @Override
    public MovieScheduleModel insertMovieSchedule(MovieScheduleModel movieScheduleModel) {
        return null;
    }

    @Override
    public MovieScheduleModel updateMovieSchedule(MovieScheduleModel movieScheduleModel) {
        return null;
    }

    @Override
    public MovieScheduleModel findCurrentMovie(String partyId, String addressId) {
        return null;
    }
}
