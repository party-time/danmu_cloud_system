package cn.partytime.listener;

import cn.partytime.config.CacheDataRepository;
import cn.partytime.model.BlockKeyQeueueModel;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;


@Component
public class RedisMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);

    @Autowired
    private CacheDataRepository cacheDataRepository;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        if(message!=null){
            String blockKeyQeueueModelStr = JSON.parseObject(String.valueOf(message),String.class).replace("'","");
            BlockKeyQeueueModel blockKeyQeueueModel = JSON.parseObject(blockKeyQeueueModelStr, BlockKeyQeueueModel.class);
            cacheDataRepository.updateCacheOne(blockKeyQeueueModel);
        }

    }
}