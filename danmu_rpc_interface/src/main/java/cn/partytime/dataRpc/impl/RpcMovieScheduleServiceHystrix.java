package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcMovieScheduleService;
import cn.partytime.model.MovieScheduleModel;

import java.util.List;

public class RpcMovieScheduleServiceHystrix implements RpcMovieScheduleService {

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
}
