package cn.partytime.scheduler;

import cn.partytime.common.cachekey.AdminUserCacheKey;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcMovieScheduleService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.MovieScheduleModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.CacheDataService;
import cn.partytime.service.CommandHanderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
@RefreshScope
public class DanmuServerScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CommandHanderService.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private RpcDanmuAddressService danmuAddressService;

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private RpcMovieScheduleService rpcMovieScheduleService;

    @Autowired
    private CacheDataService cacheDataService;

    //@Scheduled(cron = "0/10 * * * * *")
    public void checkAdminOffLineScheduler() {

        logger.info("管理员离线告警监听");
        //获取管理员掉县时间

        List<DanmuAddressModel> danmuAddressModels = danmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressModels)){
            for(DanmuAddressModel danmuAddressModel:danmuAddressModels){
                //rpcPartyService.findPartyAddressId(danmuAddressModel.getId());
                PartyLogicModel partyLogicModel = rpcPartyService.findFilmByAddressId(danmuAddressModel.getId());
                if(partyLogicModel!=null){
                    String partyId = partyLogicModel.getPartyId();
                    String addressId = partyLogicModel.getAddressId();
                    int count = danmuChannelRepository.findFilmCheckAdminCount();
                    if(count==0){
                        //判断管理员离线时间
                        Object object = redisService.get(AdminUserCacheKey.AMIN_OFFLINE_TIME);
                        if(object!=null){
                            long time = Long.parseLong(String.valueOf(object));
                            sendAlaram(time);
                        }else{
                            //管理员从来未登陆过
                            MovieScheduleModel movieScheduleModel = rpcMovieScheduleService.findCurrentMovie(partyId,addressId);
                            Date movieStartTime = movieScheduleModel.getMoviceStartTime();
                            sendAlaram(movieStartTime.getTime());
                        }
                    }
                    break;
                }
            }
        }
    }


    public void sendAlaram(long  time){
        Date date =  DateUtils.getCurrentDate();
        int alarmCount = cacheDataService.findadminOfflineAlarmCount();
        if(alarmCount>0){
            logger.info("告警已经发出");
            return;
        }


        long subTime = date.getTime() - time;
        long minute = subTime/1000/60;
        if(minute>5){
            //告警
            //rpcAdminAlarmService.admiOffLine();
            cacheDataService.adminOfflineAlarmCount(1);
        }
    }
}