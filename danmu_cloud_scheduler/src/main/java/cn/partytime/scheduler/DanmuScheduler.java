package cn.partytime.scheduler;

import cn.partytime.model.spider.Spider;
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
    private SpiderService spiderService;

    @Value("${file.doubanbackgroundPath}")
    private String backgroundPath;

    @Scheduled(cron = "0 0 16 * * ?")
    private void daobanData() throws IOException {
        String s=HttpUtils.sendGet("https://movie.douban.com/cinema/nowplaying/beijing/", null);

        Document doc = Jsoup.parse(s);
        filmData(doc,"nowplaying");

        log.info("将要进行的电影");
        filmData(doc,"upcoming");

    }

    public void filmData(Document doc,String type){
        Element content = doc.getElementById(type);
        Elements links = content.getElementsByClass("list-item");
        System.out.print("总影片数量:"+links.size());
        int status = 0;
        if("upcoming".equals(type)){
            status =1;
        }
        List<Spider> spiderList = new ArrayList<>();
        for (Element link : links) {
            String id = link.attr("id");
            String filmName = link.attr("data-title");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String url = "https://movie.douban.com/subject/"+id+"/?from=playing_poster";
            String filmContent= HttpUtils.sendGet(url, null);
            Spider spider=parseFilmContent(filmContent,id,filmName,status);

            spiderList.add(spider);
        }
        //log.info(JSON.toJSONString(spiderList));
        spiderService.saveOrUpDate(spiderList);
    }

    public Spider parseFilmContent(String content,String id,String name,int status){
        Spider filmInfo = new Spider();
        Document doc = Jsoup.parse(content);

        Element elementInfoInterestsectl = doc.getElementById("interest_sectl");
        String score = elementInfoInterestsectl.getElementsByAttributeValue("property","v:average").text();

        log.info("score:"+score);

        Elements imageElements =  doc.getElementById("mainpic").getElementsByTag("img");
        String imageUrl = imageElements.get(0).attr("src");
        log.info("imageUrl:"+imageUrl);

        try {
            String suffix = imageUrl.substring(imageUrl.lastIndexOf("."));
            log.info("Physical path:"+(backgroundPath+"/spider/douban/"+id+suffix));
            filmInfo.setImageUrl("/spider/douban/"+id+suffix);
            FileUtils.saveImage(imageUrl,backgroundPath+"/spider/douban/"+id+suffix);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Element elementInfo = doc.getElementById("info");
        Elements infos = elementInfo.getElementsByTag("span");

        filmInfo.setOldId(id);
        filmInfo.setName(name);
        filmInfo.setScore(score);
        log.info("电影名称:"+name);
        for (Element info : infos) {
            if (info.childNodeSize() > 0) {
                String key = info.getElementsByAttributeValue("class", "pl").text();
                if ("导演".equals(key)) {
                    System.out.println(info.getElementsByAttributeValue("class", "attrs").text());
                    //movie.setDirector(info.getElementsByAttributeValue("class", "attrs").text());
                } else if ("编剧".equals(key)) {
                    System.out.println(info.getElementsByAttributeValue("class", "attrs").text());
                    //movie.setScenarist(info.getElementsByAttributeValue("class", "attrs").text());
                } else if ("主演".equals(key)) {
                    System.out.println(info.getElementsByAttributeValue("class", "attrs").text());
                    ///movie.setActors(info.getElementsByAttributeValue("class", "attrs").text());
                } else if ("类型:".equals(key)) {
                    String typeStr = doc.getElementsByAttributeValue("property", "v:genre").text();
                    String []typeArray = typeStr.split(" ");
                    List<String> typeList = new ArrayList<>();
                    for(String str:typeArray){
                        log.info("type:{}",str);
                        typeList.add(str);
                    }
                    filmInfo.setTypeList(typeList);
                    ///movie.setType(doc.getElementsByAttributeValue("property", "v:genre").text());
                } else if ("制片国家/地区:".equals(key)) {
                    Pattern patternCountry = Pattern.compile(".制片国家/地区:</span>.+[\\u4e00-\\u9fa5]+.+[\\u4e00-\\u9fa5]+\\s+<br>");
                    Matcher matcherCountry = patternCountry.matcher(doc.html());
                    if (matcherCountry.find()) {
                        //movie.setCountry(matcherCountry.group().split("</span>")[1].split("<br>")[0].trim());// for example: >制片国家/地区:</span> 中国大陆 / 香港     <br>
                        System.out.print(matcherCountry.group().split("</span>")[1].split("<br>")[0].trim());
                    }
                } else if ("语言:".equals(key)) {
                    Pattern patternLanguage = Pattern.compile(".语言:</span>.+[\\u4e00-\\u9fa5]+.+[\\u4e00-\\u9fa5]+\\s+<br>");
                    Matcher matcherLanguage = patternLanguage.matcher(doc.html());
                    if (matcherLanguage.find()) {
                        //movie.setLanguage(matcherLanguage.group().split("</span>")[1].split("<br>")[0].trim());
                        System.out.println(matcherLanguage.group().split("</span>")[1].split("<br>")[0].trim());
                    }
                } else if ("上映日期:".equals(key)) {
                    //movie.setReleaseDate(doc.getElementsByAttributeValue("property", "v:initialReleaseDate").text());
                    System.out.println();
                    String dateStr = doc.getElementsByAttributeValue("property", "v:initialReleaseDate").text();
                    log.info("date:{}",dateStr);
                    String chineseDateStr = dateStr.substring(0,10);
                    log.info("中国上映日期:{}",chineseDateStr);
                    filmInfo.setDateStr(chineseDateStr);
                } else if ("片长:".equals(key)) {
                    System.out.println();
                    String timeStr = doc.getElementsByAttributeValue("property", "v:runtime").text();
                    String regex ="[\u4e00-\u9fa5]";
                    Pattern pat = Pattern.compile(regex);
                    Matcher mat = pat.matcher(timeStr);
                    String repickStr = mat.replaceAll("");
                    log.info("时长:{}",repickStr);
                    filmInfo.setTime(Integer.parseInt(repickStr==null?"0":repickStr));
                }
            }
        }
        filmInfo.setStatus(status);
        log.info("filmInfo:{}", JSON.toJSONString(filmInfo));
        return filmInfo;
    }


}
