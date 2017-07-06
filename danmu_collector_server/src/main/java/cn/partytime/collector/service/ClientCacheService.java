package cn.partytime.collector.service;

import cn.partytime.collector.config.DanmuChannelRepository;
import cn.partytime.collector.model.DanmuClientModel;
import cn.partytime.common.cachekey.DanmuCacheKey;
import cn.partytime.common.constants.ClientConst;
import cn.partytime.common.util.ListUtils;
import cn.partytime.redis.service.RedisService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lENOVO on 2016/9/7.
 */
@Service
public class ClientCacheService {

    private static final Logger logger = LoggerFactory.getLogger(ClientCacheService.class);

    @Autowired
    private RedisService redisService;


    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private ClientChannelService clientChannelService;


    /**
     * 获取下一个屏幕编号
     *
     * @param addressId
     * @return
     */
    public int getNextScreenId(String addressId, int screenId) {

        int clientType = Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN);
        List<Channel> screenChannelList = clientChannelService.findDanmuClientChannelAddressByClientType(addressId,clientType);
        List<DanmuClientModel> danmuClientModelList = new ArrayList<DanmuClientModel>();
        if(ListUtils.checkListIsNotNull(screenChannelList)){
            for(Channel channel:screenChannelList){
                DanmuClientModel danmuClientModel = danmuChannelRepository.get(channel);
                if(danmuClientModel!=null){
                    danmuClientModelList.add(danmuClientModel);
                }
            }
        }
        // 按点击数倒序
        if(ListUtils.checkListIsNotNull(danmuClientModelList)){
            Collections.sort(danmuClientModelList, new Comparator<DanmuClientModel>() {
                public int compare(DanmuClientModel arg0, DanmuClientModel arg1) {
                    int hits0 = arg0.getScreenId();
                    int hits1 = arg1.getScreenId();
                    if (hits1 < hits0) {
                        return 1;
                    } else if (hits1 == hits0) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
            for(DanmuClientModel danmuClientModel:danmuClientModelList){
                if(danmuClientModel.getScreenId()>screenId){
                    return danmuClientModel.getScreenId();
                }
            }
        }
        return 0;
    }

    /**
     * 获取第一个屏幕编号
     *
     * @param addressId
     * @return
     */
    public int getFirstScreenId(String addressId) {
        int clientType = Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN);
        List<Channel> screenChannelList = clientChannelService.findDanmuClientChannelAddressByClientType(addressId,clientType);
        List<DanmuClientModel> danmuClientModelList = new ArrayList<DanmuClientModel>();
        if(ListUtils.checkListIsNotNull(screenChannelList)){
            for(Channel channel:screenChannelList){
                DanmuClientModel danmuClientModel = danmuChannelRepository.get(channel);
                if(danmuClientModel!=null){
                    danmuClientModelList.add(danmuClientModel);
                }
            }
        }
        // 按点击数倒序
        if(ListUtils.checkListIsNotNull(danmuClientModelList)){
            Collections.sort(danmuClientModelList, new Comparator<DanmuClientModel>() {
                public int compare(DanmuClientModel arg0, DanmuClientModel arg1) {
                    int hits0 = arg0.getScreenId();
                    int hits1 = arg1.getScreenId();
                    if (hits1 < hits0) {
                        return 1;
                    } else if (hits1 == hits0) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
            int screenId = danmuClientModelList.get(0).getScreenId();
            return screenId;
        }
        return 0;
    }



    /**
     * 获取广播队列中的弹幕信息
     *
     * @param addressId
     * @return
     */
    public Object getDanmuFromPubDanmuList(String addressId) {
        String key = DanmuCacheKey.PUB_DANMU_CACHE_LIST + addressId;
        return redisService.popFromList(key);
    }

}
