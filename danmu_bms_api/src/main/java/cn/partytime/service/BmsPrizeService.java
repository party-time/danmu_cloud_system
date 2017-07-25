package cn.partytime.service;

import cn.partytime.common.util.DateUtils;
import cn.partytime.model.manager.Party;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.user.UserPrize;
import cn.partytime.rpcService.PartyLogicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by lENOVO on 2016/10/18.
 */
@Service
public class BmsPrizeService {

    private static final Logger logger = LoggerFactory.getLogger(BmsPrizeService.class);

    @Autowired
    private PartyLogicService partyLogicService;

    @Autowired
    private UserPrizeService userPrizeService;


    @Autowired
    private PartyService partyService;

    public RestResultModel sendPrize(String partyId, String openId, String danmuId, String createUserId) {

        logger.info("向活动：{},用户:{},发送奖品", partyId, openId);

        RestResultModel restResultModel = null;
        Party party = partyService.findById(partyId);
        if (party == null) {
            logger.info("活动:{}不存在", partyId);
            restResultModel = new RestResultModel();
            restResultModel.setResult(404);
            restResultModel.setData("活动不存在");
            return restResultModel;
        }

        boolean isOver = partyLogicService.checkPartyIsOver(party);
        if (isOver) {
            logger.info("活动:{}已经结束", partyId);
            restResultModel = new RestResultModel();
            restResultModel.setResult(405);
            restResultModel.setData("活动已经结束");
            return restResultModel;
        }

        //给当前弹幕的用户已经
        UserPrize userPrize = userPrizeService.findByDanmuIdAndOpenId(danmuId, openId);
        if (userPrize != null) {
            logger.info("给用户:{},弹幕:{},已经发送过奖品", openId, danmuId);
            restResultModel = new RestResultModel();
            restResultModel.setResult(201);
            restResultModel.setData("当前弹幕已经发送过奖品");
            return restResultModel;
        }

        //保存用户信息
        //TODO:掉刘伟的接口
        userPrize = saveUserPrize(danmuId, openId, createUserId);
        if(userPrize!=null){
            logger.info("给用户:{},弹幕:{},已经发送过奖品成功", openId, danmuId);
            restResultModel = new RestResultModel();
            restResultModel.setResult(200);
            restResultModel.setData("保存成功");
            return restResultModel;
        }else{
            restResultModel = new RestResultModel();
            restResultModel.setResult(500);
            restResultModel.setData("异常");
            return restResultModel;
        }
    }

    private UserPrize saveUserPrize(String danmuId, String openId, String createUserId) {
        Date date = DateUtils.getCurrentDate();
        UserPrize userPrize = new UserPrize();
        userPrize.setOpenId(openId);
        userPrize.setDanmuId(danmuId);
        userPrize.setCreateUserId(createUserId);
        userPrize.setUpdateUserId(createUserId);
        userPrize.setCreateTime(date);
        userPrize.setUpdateTime(date);
       return userPrizeService.save(userPrize);
    }


}
