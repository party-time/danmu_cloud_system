package cn.partytime.scheduler.impl;

import cn.partytime.scheduler.BaseScheduler;
import cn.partytime.service.DoubanSpiderService;
import cn.partytime.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by admin on 2018/3/20.
 */

@Service("doubanCraw")
@EnableScheduling
@RefreshScope
@Slf4j
public class DoubanCrawScheduler implements BaseScheduler {


    @Autowired
    private DoubanSpiderService doubanSpiderService;

    @Scheduled(cron = "0 0 16 * * ?")
    public void execute() throws IOException {
        try{
            String s= HttpUtils.sendGet("https://movie.douban.com/cinema/nowplaying/beijing/", null);

            Document doc = Jsoup.parse(s);
            doubanSpiderService.filmData(doc,"nowplaying");

            log.info("将要进行的电影");
            doubanSpiderService.filmData(doc,"upcoming");
        }catch (Exception e){
            log.info("爬取数据异常！");
            e.printStackTrace();
        }

    }

}
