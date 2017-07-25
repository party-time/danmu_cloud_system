package cn.partytime.controller;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.wechat.WechatUser;
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


    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(String nick , int pageNumber,int pageSize){
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

}
