package cn.partytime.service;

import cn.partytime.common.util.DateUtils;
import cn.partytime.model.BlockKeyQeueueModel;
import cn.partytime.model.manager.BlockKeyword;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by lENOVO on 2016/8/25.
 */

@Service
public class BmsBlockService {

    @Resource(name = "blockKeywordService")
    private BlockKeywordService blockKeywordService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     *  通多id删除敏感词
     * @param id
     */
    public void deleteBlockWord(String id){
        blockKeywordService.delete(id);
        BlockKeyQeueueModel blockKeyQeueueModel = new BlockKeyQeueueModel();
        blockKeyQeueueModel.setId(id);
        blockKeyQeueueModel.setStatus(2);
        //sendMessageToMq(exchangeName, JSON.toJSONString(blockKeyQeueueModel));

        redisTemplate.convertAndSend("blockKey",JSON.toJSONString(blockKeyQeueueModel));
    }

    /**
     * 添加敏感词
     * @param word
     */
    public BlockKeyword addBlockWord(String word,String userId){
        BlockKeyword blockKeyword = blockKeywordService.findByWord(word);
        if(blockKeyword ==null){

            blockKeyword = new BlockKeyword();
            Date date = DateUtils.getCurrentDate();
            blockKeyword.setWord(word);
            blockKeyword.setCreateUserId(userId);
            blockKeyword.setUpdateUserId(userId);
            blockKeyword.setUpdateTime(date);
            blockKeyword.setCreateTime(date);
            blockKeyword = blockKeywordService.save(blockKeyword);
            if(blockKeyword!=null){
                //下发通知敏感词添加
                BlockKeyQeueueModel blockKeyQeueueModel = new BlockKeyQeueueModel();
                blockKeyQeueueModel.setId(blockKeyword.getId());
                blockKeyQeueueModel.setStatus(0);
                blockKeyQeueueModel.setWord(word);
                //sendMessageToMq(exchangeName, JSON.toJSONString(blockKeyQeueueModel));
                redisTemplate.convertAndSend("blockKey",JSON.toJSONString(blockKeyQeueueModel));
            }
            return blockKeyword;
        }
        return  null;
    }



}
