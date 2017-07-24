package cn.partytime.message.wechatMessage;

import cn.partytime.message.model.Monitor;
import cn.partytime.message.rpcService.dataRpcService.AdminUserService;
import cn.partytime.message.rpcService.dataRpcService.MonitorService;
import cn.partytime.message.rpcService.dataRpcService.WechatUserService;
import cn.partytime.model.AdminUser;
import cn.partytime.model.WechatUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 2017/7/24.
 */
public class WechatMessageService {

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private WechatUserService wechatUserService;



    public void send(String key,Map<String,String> content){
        Monitor monitor = monitorService.findByKey(key);
        if( null == monitor ){
            throw new IllegalArgumentException("报警的key不存在");
        }
        if( null == monitor.getAdminUserIds() || "".equals(monitor.getAdminUserIds())){
            throw new IllegalArgumentException("报警的管理员不存在");
        }
        if(monitor.getAdminUserIds().indexOf(",") != -1){
            List<AdminUser> adminUserList = new ArrayList<>();
            String[] ids = monitor.getAdminUserIds().split(",");
            for(String id : ids){
               AdminUser adminUser =  adminUserService.findById(id);
                adminUserList.add(adminUser);
            }


        }else{
            AdminUser adminUser = adminUserService.findById(monitor.getAdminUserIds());
            WechatUser wechatUser = wechatUserService.findById(adminUser.getWechatId());

        }


    }

}
