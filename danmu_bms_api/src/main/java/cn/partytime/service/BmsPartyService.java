package cn.partytime.service;

import cn.partytime.dataService.PartyLogicService;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.PartyResult;
import cn.partytime.model.UpdatePartyModel;
import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.MovieAlias;
import cn.partytime.model.manager.Party;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.redis.service.RedisService;
import cn.partytime.repository.user.WechatUserRepository;
import cn.partytime.service.wechat.WechatUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lENOVO on 2016/9/22.
 */
@Slf4j
@Service
public class BmsPartyService {
    private static final Logger logger = LoggerFactory.getLogger(BmsPartyService.class);

    @Autowired
    private PartyLogicService partyLogicService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    @Autowired
    private WechatUserRepository wechatUserRepository;

    @Autowired
    private PartyService partyService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DanmuLibraryPartyService danmuLibraryPartyService;

    @Autowired
    private WechatUserInfoService wechatUserInfoService;



    public PartyLogicModel findCurrentParty(String openId) {
        try {
            logger.info("查询当前活动----------------------start");
            //获取用户经纬度
            WechatUser wechatUser = wechatUserRepository.findByOpenId(openId);
            WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatUser.getId());
            String userId = wechatUser.getUserId();
            logger.info("经度:{},纬度:{},用户编号:{}",wechatUserInfo.getLastLongitude(),wechatUserInfo.getLastLatitude(),userId);
            PartyLogicModel partyLogicModel =partyLogicService.findPartyByLonLat(wechatUserInfo.getLastLongitude(), wechatUserInfo.getLastLatitude());
            if (partyLogicModel == null) {
                logger.info("没有活动");
                return null;
            }
            return partyLogicModel;
        } catch (Exception e) {
            logger.error("查询当前活动:" + e.getMessage());
            return null;
        }
    }


    /**
     * type -1:全部  status:0全部 1未下线或者未结束  2已下线或者已结束
     *
     * type 1：活动  2：电影
     *
     */
    public PageResultModel findAllByPage(Integer pageNo, Integer pageSize ,Integer type,Integer status){
        Page<Party> partyPage = null;
        if( type == -1){
            partyPage = partyService.findAllPageByStatus(pageNo,pageSize,status);
        }else{
            if( -1 == status){
                partyPage = partyService.findPageByType(pageNo,pageSize,type);
            }else{
                partyPage = partyService.findPageByTypeAndStatus(pageNo,pageSize,type,status);
            }
        }
        PageResultModel pageResultModel = new PageResultModel();
        if( null != partyPage && null != partyPage.getContent()){
             List<PartyResult> partyResultList = new ArrayList<PartyResult>();
             for(Party party : partyPage.getContent()){
                 PartyResult partyResult = new PartyResult();
                 partyResult.setId(party.getId());
                 partyResult.setName(party.getName());
                 partyResult.setStartTime(party.getStartTime());
                 partyResult.setEndTime(party.getEndTime());
                 partyResult.setShortName(party.getShortName());
                 partyResult.setType(party.getType());
                 partyResult.setStatus(party.getStatus());
                 partyResult.setMovieAlias(party.getMovieAlias());
                 DanmuLibraryParty danmuLibraryParty = danmuLibraryPartyService.findByPartyId(party.getId());
                 if( null != danmuLibraryParty){
                     partyResult.setDanmuLibraryId(danmuLibraryParty.getDanmuLibraryId());
                 }
                 partyResultList.add(partyResult);
             }

            pageResultModel.setRows(partyResultList);
            pageResultModel.setTotal(partyPage.getTotalElements());
        }
        return pageResultModel;
    }

    public DanmuLibraryParty updatePartyLibrary(String partyId,String danmuLibraryId){

        return danmuLibraryPartyService.save(danmuLibraryId,partyId);
    }

    public UpdatePartyModel findByPartyId(String partyId){
        Party party = partyService.findById(partyId);
        List<MovieAlias> movieAliasList = MovieAlias.getAll();
        MovieAlias movieAlias = null;
        if( null != party && !StringUtils.isEmpty(party.getMovieAlias())){
            for( MovieAlias m : movieAliasList){
                if(party.getMovieAlias().equals(m.getValue())){
                    movieAlias = m;
                }
            }
        }

        DanmuLibraryParty danmuLibraryParty = danmuLibraryPartyService.findByPartyId(partyId);

        List<DanmuAddress> danmuAddressList = danmuAddressService.findAddressByPartyId(partyId);

        UpdatePartyModel updatePartyModel = new UpdatePartyModel();
        updatePartyModel.setParty(party);
        updatePartyModel.setDanmuAddressList(danmuAddressList);
        if( null != danmuLibraryParty){
            updatePartyModel.setDanmuLibraryId(danmuLibraryParty.getDanmuLibraryId());
        }
        updatePartyModel.setMovieAlias(movieAlias);
        return updatePartyModel;
    }

}
