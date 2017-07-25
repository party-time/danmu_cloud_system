package cn.partytime.service;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.WechatRewardResult;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.H5Template;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.WechatReward;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.service.wechat.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2016/11/29.
 */
@Service
public class BmsWechatRewardService {

    @Autowired
    private WechatRewardService wechatRewardService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private H5TemplateService h5TemplateService;

    public PageResultModel findAll(int page , int size){
        PageResultModel pageResultModel = new PageResultModel();
        Page<WechatReward> wechatRewardPage = wechatRewardService.findAll(page,size);
        if( null != wechatRewardPage && null != wechatRewardPage.getContent()){
            List<WechatRewardResult> wechatRewardResultList = new ArrayList<WechatRewardResult>();
            for(WechatReward wechatReward : wechatRewardPage.getContent()){
                WechatRewardResult wechatRewardResult = new WechatRewardResult();
                wechatRewardResult.setWechatReward(wechatReward);
                if( null != wechatReward.getPartyId()){
                    Party party = partyService.findById(wechatReward.getPartyId());
                    wechatRewardResult.setParty(party);
                }
                if( null != wechatReward.getAddressId()){
                    DanmuAddress address = danmuAddressService.findById(wechatReward.getAddressId());
                    wechatRewardResult.setDanmuAddress(address);
                }
                if( null != wechatReward.getOpenId()){
                    WechatUser wechatUser = wechatUserService.findByOpenId(wechatReward.getOpenId());
                    wechatRewardResult.setWechatUser(wechatUser);
                }

                if( null != wechatReward.getH5tempId()){
                    H5Template h5Template = h5TemplateService.findById(wechatReward.getH5tempId());
                    wechatRewardResult.setH5Template(h5Template);
                }
                wechatRewardResultList.add(wechatRewardResult);

            }
            pageResultModel.setRows(wechatRewardResultList);
            pageResultModel.setTotal(wechatRewardPage.getTotalElements());
        }

        return pageResultModel;

    }
}
