package cn.partytime.controller;

import cn.partytime.common.util.CmdConst;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.service.BmsDanmuService;
import cn.partytime.service.BmsPartyService;
import cn.partytime.service.wechat.BmsWechatUserManagerService;
import cn.partytime.service.wechat.WechatUserInfoService;
import cn.partytime.service.wechat.WechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 2016/11/28.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/wechatmanager")
@Slf4j
public class WechatUserManagerController {

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private BmsWechatUserManagerService bmsWechatUserManagerService;

    @Autowired
    private BmsDanmuService bmsDanmuService;

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private BmsPartyService bmsPartyService;


    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(String nick , int pageNumber, int pageSize){
        PageResultModel pageResultModel = new PageResultModel();
        pageNumber = pageNumber-1;
        Page<WechatUser> page = null;
        if(StringUtils.isEmpty(nick)){
            return bmsWechatUserManagerService.findAll(pageNumber,pageSize);
        }else{
            return bmsWechatUserManagerService.findByNickLike(nick,pageNumber,pageSize);
        }
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel delById(String id){
        RestResultModel restResultModel = new RestResultModel();
        bmsWechatUserManagerService.deleteById(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/initWechatUserInfo", method = RequestMethod.GET)
    public RestResultModel initWechatUserInfo(){
        RestResultModel restResultModel = new RestResultModel();
        wechatUserInfoService.initWechatUserInfo();
        restResultModel.setResult(200);
        return restResultModel;
    }

    /**
     * 分配用户的场地
     * @param wechatId
     * @return
     */
    @RequestMapping(value = "/assignAddress", method = RequestMethod.GET)
    public RestResultModel assignAddress(String addressId,String wechatId){
        RestResultModel restResultModel = new RestResultModel();
        bmsWechatUserManagerService.assignAddress(addressId,wechatId);
        restResultModel.setResult(200);
        return restResultModel;
    }

    /**
     * 用户表白
     * @param wechatId
     * @return
     */
    @RequestMapping(value = "/sendBiaobai", method = RequestMethod.GET)
    public RestResultModel sendBiaobai(String addressId,String wechatId){
        RestResultModel restResultModel = new RestResultModel();
        WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatId);
        WechatUser wechatUser = wechatUserService.findById(wechatId);
        Map<String,String> danmuMap = new HashMap<>();
        danmuMap.put("nameA","张三");
        danmuMap.put("nameB","李四");
        danmuMap.put("idd","5");
        danmuMap.put("content","今天喝酒，明天吃饭，后台逛大街");

        String openId = wechatUser.getOpenId();

        DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findAddressByLonLat(wechatUserInfo.getLastLongitude(),wechatUserInfo.getLastLatitude());

        PartyLogicModel partyLogicModel = bmsPartyService.findCurrentParty(openId);

        bmsDanmuService.sendDanmuByWechat(CmdConst.CMD_NAME_BIAOBAI, danmuMap, openId, partyLogicModel.getPartyId(), danmuAddressModel.getId(), 1,0);
        restResultModel.setResult(200);
        return restResultModel;
    }

    /**
     * 用户表白
     * @param wechatId
     * @return
     */
    @RequestMapping(value = "/dashang", method = RequestMethod.GET)
    public RestResultModel dashang(String addressId,String wechatId){

        RestResultModel restResultModel = new RestResultModel();
        WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatId);
        WechatUser wechatUser = wechatUserService.findById(wechatId);
        Map<String,String> map = new HashMap<>();
        map.put("name",wechatUser.getNick());
        map.put("gift","飞龙在天");

        String openId = wechatUser.getOpenId();

        DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findAddressByLonLat(wechatUserInfo.getLastLongitude(),wechatUserInfo.getLastLatitude());

        PartyLogicModel partyLogicModel = bmsPartyService.findCurrentParty(openId);

        bmsDanmuService.sendDanmuByWechat(CmdConst.CMD_NAME_MONEY, map, openId, "5ad6bf9b47f1fd2074dceaec", "5a4d9c04e2f0d248cd43f412", 1,0);
        restResultModel.setResult(200);
        return restResultModel;
    }
}
