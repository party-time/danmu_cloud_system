package cn.partytime.check.service;

import cn.partytime.check.model.DanmuClientModel;
import cn.partytime.common.cachekey.ClientCacheKey;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lENOVO on 2016/9/26.
 */

@Service
public class CacheDataService {


    @Autowired
    private RedisService redisService;




    /**
     * 获取缓存中存的客户端
     * @param addressId
     * @return
     */
    public List<DanmuClientModel> findDanmuClientList(String addressId) {
        String clientSortSetKey = ClientCacheKey.CLIENT_CACHE_ID_SORTSET + addressId;
        Set<String> clientSet = redisService.getSortSetByRnage(clientSortSetKey, 0, -1, true);
        List<DanmuClientModel> clientNameList = null;
        if (clientSet != null && !clientSet.isEmpty()) {
            clientNameList = new ArrayList<DanmuClientModel>();
            for (String str : clientSet) {
                DanmuClientModel danmuClientModel = JSON.parseObject(str, DanmuClientModel.class);
                clientNameList.add(danmuClientModel);
            }
        }
        return clientNameList;
    }
}
