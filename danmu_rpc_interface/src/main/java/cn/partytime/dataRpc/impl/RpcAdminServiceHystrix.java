package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcAdminService;
import cn.partytime.model.AdminUserDto;
import org.springframework.stereotype.Component;

@Component
public class RpcAdminServiceHystrix implements RpcAdminService {
    @Override
    public Boolean checkAuthKey(String authKey) {
        return null;
    }

    @Override
    public AdminUserDto findById(String id) {
        return null;
    }

    @Override
    public AdminUserDto getAdminUser(String authKey) {
        return null;
    }

    @Override
    public AdminUserDto updateCheckFlg(String id, Integer checkFlg) {
        return null;
    }


}
