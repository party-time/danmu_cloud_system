package cn.partytime.scheduler;

import cn.partytime.alarmRpc.RpcMovieAlarmService;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcMovieScheduleService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.MovieScheduleModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.spider.Spider;
import cn.partytime.service.DoubanSpiderService;
import cn.partytime.service.spider.SpiderService;
import cn.partytime.util.FileUtils;
import cn.partytime.util.HttpUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2018/1/2.
 */

@Component
@EnableScheduling
@RefreshScope
@Slf4j
public class DanmuScheduler {

    @Autowired
    private DoubanSpiderService doubanSpiderService;

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private RpcPartyService rpcPartyService;


    @Autowired
    private RpcMovieAlarmService rpcMovieAlarmService;

    @Autowired
    private RpcMovieScheduleService rpcMovieScheduleService;

    @Scheduled(cron = "0 0 16 * * ?")
    private void daobanData() throws IOException {
        String s=HttpUtils.sendGet("https://movie.douban.com/cinema/nowplaying/beijing/", null);

        Document doc = Jsoup.parse(s);
        doubanSpiderService.filmData(doc,"nowplaying");

        log.info("将要进行的电影");
        doubanSpiderService.filmData(doc,"upcoming");

    }

    /**
     * 电影超时告警
     */
    @Scheduled(cron = "0/30 * * * * ?")
    private void moviePlayTimeListener() {
        log.info("电影播放时长告警");
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if (ListUtils.checkListIsNotNull(danmuAddressList)) {
            for (DanmuAddressModel danmuAddress : danmuAddressList) {
                String addressId = danmuAddress.getId();
                PartyLogicModel partyLogicModel = rpcPartyService.findPartyByAddressId(addressId);
                if (partyLogicModel != null) {
                    String partyId = partyLogicModel.getPartyId();
                    long movieTime = partyLogicModel.getMovieTime();
                    List<MovieScheduleModel>  movieScheduleList =  rpcMovieScheduleService.findByPartyIdAndAddressId(partyId,addressId);
                    if(ListUtils.checkListIsNotNull(movieScheduleList)){
                        MovieScheduleModel movieSchedule = movieScheduleList.get(0);
                        Date movieStartTime = movieSchedule.getMoviceStartTime();
                        rpcMovieAlarmService.overTime(partyId,addressId,movieStartTime==null?0:movieStartTime.getTime(),movieTime==0?150:movieTime);
                    }
                }
            }
        }
    }
}
