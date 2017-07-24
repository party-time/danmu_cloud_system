package cn.partytime.model;

import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.MovieSchedule;

/**
 * Created by administrator on 2017/1/20.
 */
public class MovieScheduleResult {

    private MovieSchedule movieSchedule;

    private DanmuAddress danmuAddress;

    public MovieSchedule getMovieSchedule() {
        return movieSchedule;
    }

    public void setMovieSchedule(MovieSchedule movieSchedule) {
        this.movieSchedule = movieSchedule;
    }

    public DanmuAddress getDanmuAddress() {
        return danmuAddress;
    }

    public void setDanmuAddress(DanmuAddress danmuAddress) {
        this.danmuAddress = danmuAddress;
    }
}
