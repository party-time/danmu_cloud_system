package cn.partytime.service.wechat;

import cn.partytime.logicService.DanmuAddressLogicService;
import cn.partytime.logic.danmu.PageResultModel;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.model.wechat.WechatUserListModel;
import cn.partytime.service.DanmuAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 2017/4/14.
 */
@Service
public class BmsWechatUserManagerService {

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private DanmuAddressLogicService danmuAddressLogicService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    private PageResultModel findByWechatPage(Page<WechatUser> wechatUserPage ){
        if( null != wechatUserPage){
            List<String> wechatIdList = new ArrayList<>();
            for(WechatUser wechatUser : wechatUserPage.getContent()){
                wechatIdList.add(wechatUser.getId());
            }
            List<WechatUserInfo> wechatUserInfoList = wechatUserInfoService.findByWechatIds(wechatIdList);
            List<WechatUserListModel> wechatUserListModelList = new ArrayList<>();
            for(WechatUser wechatUser : wechatUserPage.getContent()){
                WechatUserListModel wechatUserListModel = new WechatUserListModel();
                wechatUserListModel.setWechatUser(wechatUser);
                for(WechatUserInfo wechatUserInfo : wechatUserInfoList){
                    if(wechatUser.getId().equals(wechatUserInfo.getWechatId())){
                        wechatUserListModel.setWechatUserInfo(wechatUserInfo);
                        if( null != wechatUserInfo.getRegistLongitude() && null != wechatUserInfo.getRegistLatitude()){
                            DanmuAddress registAddress = danmuAddressLogicService.findAddressByLonLat(wechatUserInfo.getRegistLongitude(),wechatUserInfo.getRegistLatitude());
                            wechatUserListModel.setRegistAddress(registAddress);
                        }
                        if( null != wechatUserInfo.getLastLongitude() && null != wechatUserInfo.getLastLatitude() ){
                            DanmuAddress lastAddress = danmuAddressLogicService.findAddressByLonLat(wechatUserInfo.getLastLongitude(),wechatUserInfo.getLastLatitude());
                            wechatUserListModel.setLastAddress(lastAddress);
                        }
                    }
                }
                wechatUserListModelList.add(wechatUserListModel);
            }

            PageResultModel pageResultModel = new PageResultModel();
            pageResultModel.setTotal(wechatUserPage.getTotalElements());
            pageResultModel.setRows(wechatUserListModelList);
            return pageResultModel;
        }else{
            return null;
        }
    }


    public PageResultModel findAll(int pageNumber, int pageSize){
        Page<WechatUser> wechatUserPage = wechatUserService.findAll(pageNumber,pageSize);
        return findByWechatPage(wechatUserPage);
    }

    public PageResultModel findByNickLike(String nick,int pageNumber, int pageSize){
        Page<WechatUser> wechatUserPage = wechatUserService.findByNickLike(nick,pageNumber,pageSize);
        return findByWechatPage(wechatUserPage);
    }

    public void deleteById(String id){
        wechatUserService.delById(id);
        wechatUserInfoService.delbyWechatId(id);
    }

    public DanmuAddress findUseAddressByWechatId(String wechatId){
        WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatId);
        if( null == wechatUserInfo){
            return null;
        }
        return danmuAddressLogicService.findAddressByLonLat(wechatUserInfo.getLastLongitude(),wechatUserInfo.getLastLongitude());
    }

    public void assignAddress(String addressId,String wechatId){
        DanmuAddress danmuAddress = danmuAddressService.findById(addressId);
        WechatUser wechatUser = wechatUserService.findById(wechatId);
        WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatId);
        if( null != danmuAddress && null != wechatUser){
            wechatUser.setAssignAddressTime(new Date());
            List<Double> coordinates = danmuAddress.getLocation().getCoordinates();
            Double danmuParty_longitude = coordinates.get(0);
            Double danmuParty_latitude = coordinates.get(1);
            wechatUserInfo.setLastLatitude(danmuParty_latitude);
            wechatUserInfo.setLastLongitude(danmuParty_longitude);
            wechatUserInfoService.update(wechatUserInfo);
            wechatUserService.updateUserInfo(wechatUser);
        }
    }
}
