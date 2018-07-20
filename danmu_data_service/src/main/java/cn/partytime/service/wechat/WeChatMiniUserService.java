package cn.partytime.service.wechat;

import cn.partytime.model.wechat.WeChatMiniUser;
import cn.partytime.repository.user.WeChatMiniUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/7/20 0020.
 */

@Slf4j
@Service
public class WeChatMiniUserService {

    @Autowired
    private WeChatMiniUserRepository weChatMiniUserRepository;


    public WeChatMiniUser findByUnionId(String unionId){
        return weChatMiniUserRepository.findByUnionId(unionId);
    }

    public void saveWeChatMiniUser(WeChatMiniUser weChatMiniUser){
        weChatMiniUserRepository.save(weChatMiniUser);
    }
}
