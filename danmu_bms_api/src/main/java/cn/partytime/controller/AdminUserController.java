package cn.partytime.controller;

import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.AdminRole;
import cn.partytime.model.manager.AdminUser;
import cn.partytime.service.AdminRoleService;
import cn.partytime.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by administrator on 2016/11/8.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/adminUser")
@Slf4j
public class AdminUserController extends BaseAdminController{

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminRoleService adminRoleService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer pageNumber, Integer pageSize){
        /**
        if( !"admin".equals(getAdminUser().getUserName()) ){
            throw new IllegalArgumentException("只有管理员才可以使用");
        }
         **/
        pageNumber = pageNumber-1;
        Page<AdminUser> adminUserPage = adminUserService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(adminUserPage.getTotalElements());
        pageResultModel.setRows(adminUserPage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(String name, String password, String nick,String roleId,String weChatId){
        /*if( !"admin".equals(getAdminUser().getUserName()) ){
            throw new IllegalArgumentException("只有管理员才可以使用");
        }*/
        RestResultModel restResultModel = new RestResultModel();
        adminUserService.createAdminUser(name,password,nick,roleId,weChatId);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public RestResultModel findAdminUser(String nick,String userName){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        if(!StringUtils.isEmpty(nick) && adminUserService.isExistNick(nick)){
            restResultModel.setResult(401);
            restResultModel.setResult_msg("姓名有重复，请重新输入");
        }

        if(!StringUtils.isEmpty(userName) && adminUserService.isExistUserName(userName)){
            restResultModel.setResult(401);
            restResultModel.setResult_msg("账号有重复，请重新输入");
        }

        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(String id,String name, String password, String nick,String roleId,String weChatId){
        /*if( !"admin".equals(getAdminUser().getUserName()) ){
            throw new IllegalArgumentException("只有管理员才可以使用");
        }*/
        RestResultModel restResultModel = new RestResultModel();
        AdminUser adminUser = new AdminUser();
        adminUser.setId(id);
        adminUser.setUserName(name);
        adminUser.setPassword(password);
        adminUser.setNick(nick);
        adminUser.setRoleId(roleId);
        adminUser.setWechatId(weChatId);
        adminUserService.updateAdminInfoById(adminUser);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        /*if( !"admin".equals(getAdminUser().getUserName()) ){
            throw new IllegalArgumentException("只有管理员才可以使用");
        }*/
        RestResultModel restResultModel = new RestResultModel();
        adminUserService.deleteById(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public RestResultModel updatePassword(String oldPassword,String newPassword){
        RestResultModel restResultModel = new RestResultModel();
        if(adminUserService.checkPassword(getAdminUser().getId(),oldPassword)){
            AdminUser adminUser = new AdminUser();
            adminUser.setId(getAdminUser().getId());
            adminUser.setPassword(newPassword);
            adminUserService.updateAdminInfoById(adminUser);
            restResultModel.setResult(200);
        }else{
            restResultModel.setResult(406);
        }

        return restResultModel;
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public RestResultModel findAdminUserById(String id){
        RestResultModel restResultModel = new RestResultModel();
        AdminUser adminUser = adminUserService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(adminUser);
        return restResultModel;
    }

    @RequestMapping(value = "/findRole", method = RequestMethod.GET)
    public RestResultModel findRole(){
        RestResultModel restResultModel = new RestResultModel();
        List<AdminRole> adminRoleList = adminRoleService.findAll();
        restResultModel.setResult(200);
        restResultModel.setData(adminRoleList);
        return restResultModel;
    }


    @RequestMapping(value = "/createRole", method = RequestMethod.GET)
    public RestResultModel createRole(){
        RestResultModel restResultModel = new RestResultModel();
        AdminRole adminRole = new AdminRole();
        adminRole.setRoleName("第三方发货员");
        adminRoleService.save(adminRole);
        restResultModel.setResult(200);
        return restResultModel;
    }




}
