package cn.partytime.service;

import cn.partytime.model.LovePayDTO;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.LovePay;
import cn.partytime.model.manager.Party;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.service.wechat.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2017/7/25.
 */
@Service
public class BmsLovePayservice {

    @Autowired
    private LovePayService lovePayService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    public PageResultModel findAll(Integer page, Integer pageSize){

        PageResultModel pageResultModel = new PageResultModel();

        Page<LovePay> lovePayPage = lovePayService.findAll(page,pageSize);
        if( null != lovePayPage){
            List<LovePayDTO> lovePayDTOList = new ArrayList<>();
            for(LovePay lovePay : lovePayPage.getContent()){
                LovePayDTO lovePayDTO = new LovePayDTO();
                if(!StringUtils.isEmpty(lovePay.getOpenId())){
                    WechatUser wechatUser = wechatUserService.findByOpenId(lovePay.getOpenId());
                    lovePayDTO.setWechatUser(wechatUser);
                }
                if(!StringUtils.isEmpty(lovePay.getPartyId())){
                    Party party = partyService.findById(lovePay.getPartyId());
                    lovePayDTO.setParty(party);
                }
                if(!StringUtils.isEmpty(lovePay.getAddressId())){
                    DanmuAddress danmuAddress = danmuAddressService.findById(lovePay.getAddressId());
                    lovePayDTO.setDanmuAddress(danmuAddress);
                }
                lovePayDTO.setLovePay(lovePay);
                lovePayDTOList.add(lovePayDTO);
            }

            pageResultModel.setRows(lovePayDTOList);
            pageResultModel.setTotal(lovePayPage.getTotalElements());
        }

        return pageResultModel;
    }
}
