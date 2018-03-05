package cn.partytime.scheduler;

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

    //@Scheduled(cron = "0 0 16 * * ?")
    @PostConstruct
    private void daobanData() throws IOException {
        String s=HttpUtils.sendGet("https://movie.douban.com/cinema/nowplaying/beijing/", null);

        Document doc = Jsoup.parse(s);
        doubanSpiderService.filmData(doc,"nowplaying");

        log.info("将要进行的电影");
        doubanSpiderService.filmData(doc,"upcoming");

    }


}
