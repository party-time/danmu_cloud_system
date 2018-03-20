package cn.partytime.scheduler.impl;

import cn.partytime.alarmRpc.RpcMovieAlarmService;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcMovieScheduleService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.MovieScheduleModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.scheduler.BaseScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2018/1/2.
 */

@Service("danmuScheduler")
@EnableScheduling
@RefreshScope
@Slf4j
public class DanmuScheduler implements BaseScheduler {



    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private RpcPartyService rpcPartyService;


    @Autowired
    private RpcMovieAlarmService rpcMovieAlarmService;

    @Autowired
    private RpcMovieScheduleService rpcMovieScheduleService;

    @Autowired
    private ServletContext servletContext;

    /*@PostConstruct
    public void setData() throws IOException {
        //redisService.set("22222222222222222222222222222222","22222");
        //ClassDemo classDemo = (ClassDemo)applicationContext.getBean("first");
        //stem.out.println(classDemo);
        ApplicationContext ac1 = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        BaseClass bb = (BaseClass)ac1.getBean("firstClass");
        bb.execute();

        bb = (BaseClass)ac1.getBean("secondClass");
        bb.execute();
        //'System.out.println(bb.getClass().getName());
    }*/


    /*@Bean(name="testDemo")
    public void generateDemo() {
        System.out.println("===========================");
    }*/

    /**
     * 电影超时告警
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void execute() {
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
