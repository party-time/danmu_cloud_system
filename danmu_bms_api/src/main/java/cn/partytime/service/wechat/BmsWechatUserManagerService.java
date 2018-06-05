package cn.partytime.service.wechat;


import cn.partytime.common.constants.DistanceConst;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.model.wechat.WechatUserListModel;
import cn.partytime.service.DanmuAddressService;
import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 2017/4/14.
 */

@Slf4j
@Service
public class BmsWechatUserManagerService {

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    @Resource(name = "managerMongoTemplate")
    private MongoTemplate managerMongoTemplate;

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
                            DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findAddressByLonLat(wechatUserInfo.getRegistLongitude(),wechatUserInfo.getRegistLatitude());
                            log.info("通过经纬度:{},{}，获取的地址信息,{}",wechatUserInfo.getRegistLongitude(),wechatUserInfo.getRegistLatitude(), JSON.toJSONString(danmuAddressModel));
                            DanmuAddress danmuAddress = new DanmuAddress();
                            if(danmuAddressModel!=null){
                                BeanUtils.copyProperties(danmuAddressModel,danmuAddress);
                            }
                            wechatUserListModel.setRegistAddress(danmuAddress);
                        }
                        if( null != wechatUserInfo.getLastLongitude() && null != wechatUserInfo.getLastLatitude() ){
                            DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findAddressByLonLat(wechatUserInfo.getLastLongitude(),wechatUserInfo.getLastLatitude());
                            log.info("通过经纬度:{},{}，获取的地址信息,{}",wechatUserInfo.getRegistLongitude(),wechatUserInfo.getRegistLatitude(), JSON.toJSONString(danmuAddressModel));
                            DanmuAddress danmuAddress = new DanmuAddress();
                            if(danmuAddressModel!=null){
                                BeanUtils.copyProperties(danmuAddressModel,danmuAddress);
                            }
                            wechatUserListModel.setLastAddress(danmuAddress);
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

    public DanmuAddressModel findUseAddressByWechatId(String wechatId){
        WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatId);
        if( null == wechatUserInfo){
            return null;
        }
        return rpcDanmuAddressService.findAddressByLonLat(wechatUserInfo.getLastLongitude(),wechatUserInfo.getLastLongitude());
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


    public PageResultModel searchWechatUser(String nick,String addressId,Date startRegistTime,Date endRegistTime,Integer status){

        BasicDBObject basicDBObject4 = null;
        if(StringUtils.isEmpty(addressId)){

            DanmuAddress danmuAddress = danmuAddressService.findById(addressId);
            basicDBObject4 = new BasicDBObject("coordinates", danmuAddress.getLocation());
        }

        BasicDBObject geometryObject = new BasicDBObject("$geometry", basicDBObject4);
        geometryObject.append("$maxDistance", DistanceConst.WEChAT_CLIENT_DISTANCE);
        BasicDBObject nearObject = new BasicDBObject("$near", geometryObject);
        BasicDBObject basicDBObject1 = new BasicDBObject("location", nearObject);

        DBCursor cur1 = managerMongoTemplate.getCollection("danmu_address").find(basicDBObject1);
        PageResultModel pageResultModel = new PageResultModel();

        return pageResultModel;
    }
}
