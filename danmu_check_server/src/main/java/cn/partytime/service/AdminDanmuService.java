package cn.partytime.service;


import cn.partytime.cache.danmu.DanmuCacheService;
import cn.partytime.common.constants.PartyConst;
import cn.partytime.common.util.SetUtils;
import cn.partytime.dataRpc.RpcDanmuService;
import cn.partytime.model.DanmuLogModel;
import cn.partytime.model.DanmuModel;
import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class AdminDanmuService {


    @Autowired
    private DanmuCacheService danmuCacheService;

    public void pushTempDanmuToCheckUser(int partyType,String adminId,String partyId,Channel channel){
        log.info("*********************************************************");
        if(partyType == PartyConst.PARTY_TYPE_PARTY){
            pushPartyDanmuToChecker(partyId,adminId,channel);
        }else if(partyType == PartyConst.PARTY_TYPE_FILM){
            pushFilmDanmuToChecker(adminId,channel);
        }
    }

    public void pushPartyDanmuToChecker(String partyId,String adminId,Channel channel){
        log.info("将管理员自己未审核的弹幕推送给自己（活动）");
        Set<String> danmuSet =  danmuCacheService.getAllPartyDanmuFromCheckUserSortSet(partyId,adminId);
        if(SetUtils.checkSetIsNotNull(danmuSet)){
            for(String danmuId:danmuSet){

                Object object = danmuCacheService.getSendDanmuInfo(danmuId);
                if(object!=null){
                    log.info(JSON.toJSONString(object));
                    Map<String, Object> danmuMap = (Map<String, Object>) JSON.parse(JSON.toJSONString(object));
                    channel.write(new TextWebSocketFrame(JSON.toJSONString(danmuMap)));
                }
            }
        }
    }

    public void pushFilmDanmuToChecker(String adminId,Channel channel){
        log.info("将管理员自己未审核的弹幕推送给自己（电影）");
        Set<String> danmuSet =  danmuCacheService.getAllFilmDanmuFromCheckUserSortSet(adminId);
        if(SetUtils.checkSetIsNotNull(danmuSet)){
            for(String danmuId:danmuSet){
                Object object = danmuCacheService.getSendDanmuInfo(danmuId);
                if(object!=null){
                    log.info(JSON.toJSONString(object));
                    Map<String, Object> danmuMap = (Map<String, Object>) JSON.parse(JSON.toJSONString(object));
                    channel.write(new TextWebSocketFrame(JSON.toJSONString(danmuMap)));
                }
            }
        }
    }

}
