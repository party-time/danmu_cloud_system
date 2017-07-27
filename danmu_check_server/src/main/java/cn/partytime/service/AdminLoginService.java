package cn.partytime.service;

import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.dataRpc.RpcAdminService;
import cn.partytime.model.AdminTaskModel;
import cn.partytime.model.AdminUserDto;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dm on 2017/7/7.
 */

@Service
public class AdminLoginService {

    @Autowired
    private RpcAdminService rpcAdminService;

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;


    public boolean isLogin(String auth_key){
        return  rpcAdminService.checkAuthKey(auth_key);
    }

    public void adminLogin(String auth_key, Channel channel, int partyType){
        AdminUserDto adminUser = rpcAdminService.getAdminUser(auth_key);
        AdminTaskModel adminTaskModel = new AdminTaskModel();
        adminTaskModel.setPartyType(partyType);
        adminTaskModel.setAuthKey(auth_key);
        adminTaskModel.setAdminId(channel.id().asLongText());
        adminTaskModel.setAdminName(adminUser.getUserName());
        danmuChannelRepository.saveChannelAdminRelation(partyType,channel,adminTaskModel);
    }
}
