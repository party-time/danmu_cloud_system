package cn.partytime.logicService;

import cn.partytime.common.cachekey.FunctionControlCacheKey;
import cn.partytime.common.constants.PartyConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.MovieSchedule;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.DanmuAddressService;
import cn.partytime.service.MovieScheduleService;
import cn.partytime.service.PartyAddressRelationService;
import cn.partytime.service.PartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dm on 2017/7/12.
 */

@Service
public class PartyLogicService {

    private static final Logger logger = LoggerFactory.getLogger(PartyLogicService.class);

    @Autowired
    private PartyService partyService;

    @Autowired
    private PartyAddressRelationService partyAddressRelationService;


    @Autowired
    private MovieScheduleService movieScheduleService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    public Party getPartyId(String addressId) {
        Party partyResult = null;
        //通过日期查找活动
        List<Party> partyList = partyService.findPartyByTime(0);
        if (partyList == null || partyList.isEmpty()) {
            return null;
        }
        List<Party> listTemp = new ArrayList<Party>();
        for (Party party : partyList) {
            if (party.getStatus() != 3) {
                listTemp.add(party);
            }
        }
        if (ListUtils.checkListIsNull(listTemp)) {
            return null;
        }
        //通过地点与活动来决定唯一性
        List<String> partyIdList = new ArrayList<String>();
        listTemp.stream().forEach(party -> partyIdList.add(party.getId()));
        PartyAddressRelation partyAddressRelation = partyAddressRelationService.findByAddressIdANDPartyIdWithin(addressId, partyIdList);
        if (partyAddressRelation == null) {
            return null;
        }
        for (Party party : listTemp) {
            if (party.getId().equals(partyAddressRelation.getPartyId())) {
                partyResult = party;
                break;
            }
        }
        return partyResult;
    }

    public PartyLogicModel findPartyAddressId(String addressId){
        DanmuAddress danmuAddress = danmuAddressService.findById(addressId);
        PartyLogicModel partyLogicModel = null;
        //场地类型 0 固定场地  1 临时场地
        int danmuAddressType = danmuAddress.getType();
        if(danmuAddressType==1){
            partyLogicModel =findTemporaryParty(addressId);
            if(partyLogicModel==null){
                partyLogicModel = findFilmParty(addressId);
            }
        }else if(danmuAddressType==0){
            //如果是固定场获取的是电影信息
            partyLogicModel = findFilmParty(addressId);
            if(partyLogicModel==null){
                partyLogicModel =findTemporaryParty(addressId);
            }
        }
        if(partyLogicModel!=null){
            partyLogicModel.setAddressType(danmuAddressType);
        }
        return partyLogicModel;
    }

    public PartyLogicModel findTemporaryParty(String addressId){
        PartyLogicModel partyLogicModel = null;
        Party party = getPartyId(addressId);
        //如果是电影场，获取的是活动信息
        if(party!=null){
            partyLogicModel = new PartyLogicModel();
            //0:活动场：1：弹幕场
            partyLogicModel.setType(party.getType());
            partyLogicModel.setPartyId(party.getId());
            partyLogicModel.setAddressId(addressId);
            partyLogicModel.setPartyName(party.getName());
            partyLogicModel.setStatus(party.getStatus());
            partyLogicModel.setStartTime(party.getStartTime());
            partyLogicModel.setActiveTime(party.getActivityStartTime());
            partyLogicModel.setShortName(party.getShortName());
            partyLogicModel.setH5TempId(party.getH5TempId());

            String key = FunctionControlCacheKey.FUNCITON_CONTROL_DANMU_DENSITY + partyLogicModel.getPartyId();
            Object object = redisService.get(key);
            if(object!=null){
                partyLogicModel.setDmDensity(IntegerUtils.objectConvertToInt(object));
            }else{
                partyLogicModel.setDmDensity(PartyConst.danmuDensity);
            }
        }
        return partyLogicModel;
    }


    private PartyLogicModel findFilmParty(String addressId){
        PartyLogicModel partyLogicModel = null;
        Page<MovieSchedule> movieSchedulePage =  movieScheduleService.findAllByAddressId(addressId,1,0);
        List<MovieSchedule> movieScheduleList = movieSchedulePage.getContent();
        if(ListUtils.checkListIsNotNull(movieScheduleList)){
            MovieSchedule movieSchedule = movieScheduleList.get(0);

            //最后一条数据的结束时间不为空，没有正在进行的活动
            if(movieSchedule.getEndTime()!=null){
                logger.info("最后一场电影的已经结束!");
                return null;
            }
            //如果最后一条数据只有弹幕开始时间或者电影开始时间
            //1。如果只有弹幕开始时间 当前时间当前时间-弹幕开始时间>180分钟 认为当前时间没有电影正在进行
            //2.如果只有电影开始时间  当前时间当前时间-电影开始时间>150分钟 认为当前时间没有电影正在进行
            Date startDate = movieSchedule.getStartTime();
            Date moviceStartDate = movieSchedule.getMoviceStartTime();
            if(startDate!=null){
                long minute = DateUtils.subMinute(startDate, DateUtils.getCurrentDate());
                if(minute>190){
                    logger.info("最后一条数据: 当前时间当前时间-弹幕开始时间>180分钟");
                    return null;
                }
            }else{
                long minute = DateUtils.subMinute(moviceStartDate, DateUtils.getCurrentDate());
                if(minute>180){
                    logger.info("最后一条数据: 当前时间当前时间-电影开始时间>150分钟");
                    return null;
                }
            }
            String partyId = movieSchedule.getPartyId();
            //获取活动信息
            Party party =partyService.findById(partyId);
            if(party!=null){
                partyLogicModel = new PartyLogicModel();
                partyLogicModel.setPartyId(partyId);
                partyLogicModel.setAddressId(addressId);
                partyLogicModel.setPartyName(party.getName());
                if(movieSchedule.getClientStartTime()!=null){
                    partyLogicModel.setStartTime(new Date(movieSchedule.getClientStartTime()));
                }
                if(movieSchedule.getClientMoviceStartTime()!=null){
                    partyLogicModel.setActiveTime(new Date(movieSchedule.getClientMoviceStartTime()));
                }
                if(movieSchedule.getMoviceStartTime()!=null){
                    logger.info("电影场，活动:{},电影开始",party);
                    partyLogicModel.setStatus(2);
                }else{
                    logger.info("电影场，活动:{},弹幕开始",party);
                    partyLogicModel.setStatus(1);
                }
                partyLogicModel.setType(party.getType());
                partyLogicModel.setShortName(party.getShortName());
                partyLogicModel.setH5TempId(party.getH5TempId());
                partyLogicModel.setDmDensity(party.getDmDensity());

                String key = FunctionControlCacheKey.FUNCITON_CONTROL_DANMU_DENSITY + partyLogicModel.getPartyId();
                Object object = redisService.get(key);
                if(object!=null){
                    partyLogicModel.setDmDensity(IntegerUtils.objectConvertToInt(object));
                }else{
                    partyLogicModel.setDmDensity(party.getDmDensity());
                }

            }
        }
        return partyLogicModel;
    }

}
