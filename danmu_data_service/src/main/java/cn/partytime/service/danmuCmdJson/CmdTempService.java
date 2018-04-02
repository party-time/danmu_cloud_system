package cn.partytime.service.danmuCmdJson;

import cn.partytime.common.cachekey.CmdTempCacheKey;
import cn.partytime.model.manager.danmuCmdJson.CmdTemp;
import cn.partytime.redis.service.RedisService;
import cn.partytime.repository.manager.danmuCmdJson.CmdJsonTempRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2017/5/8.
 */

@Service
@Slf4j
public class CmdTempService {

    @Autowired
    private CmdJsonTempRepository cmdJsonTempRepository;

    @Autowired
    private RedisService redisService;

    public CmdTemp save(String name, String key,Integer isInDanmuLib,Integer isSendH5,Integer sort,Integer show,Integer type){
        CmdTemp cmdTemp = new CmdTemp();
        cmdTemp.setName(name);
        cmdTemp.setKey(key);
        cmdTemp.setIsInDanmuLib(isInDanmuLib);
        cmdTemp.setIsSendH5(isSendH5);
        cmdTemp.setSort(sort);
        cmdTemp.setShow(show);
        cmdTemp.setType(type);
        return cmdJsonTempRepository.save(cmdTemp);
    }

    public CmdTemp update(String id,String name,Integer isInDanmuLib,Integer isSendH5,Integer sort,Integer show,Integer type){
        CmdTemp cmdTemp = cmdJsonTempRepository.findOne(id);
        if( null != cmdTemp) {
            cmdTemp.setName(name);
            cmdTemp.setIsInDanmuLib(isInDanmuLib);
            cmdTemp.setIsSendH5(isSendH5);
            cmdTemp.setSort(sort);
            cmdTemp.setShow(show);
            cmdTemp.setType(type);
        }
        return cmdJsonTempRepository.save(cmdTemp);
    }


    public Page<CmdTemp> findAll(Integer page, Integer size){
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return cmdJsonTempRepository.findAll(pageRequest);
    }

    public Page<CmdTemp> findPageByType(Integer type,Integer page, Integer size){
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return cmdJsonTempRepository.findByType(type,pageRequest);
    }

    public CmdTemp findById(String id){
        return cmdJsonTempRepository.findOne(id);
    }

    public List<CmdTemp> findByIds(List<String> idList){
        return cmdJsonTempRepository.findByIdIn(idList);
    }

    public List<CmdTemp> findAll(){
        return cmdJsonTempRepository.findAll();
    }

    public CmdTemp findByKey(String key){
        return cmdJsonTempRepository.findByKey(key);
    }

    public Integer countByKey(String key){
        return cmdJsonTempRepository.countByKey(key);
    }

    public void del(String id){
        CmdTemp cmdTemp = cmdJsonTempRepository.findOne(id);
        if( null != cmdTemp){
            String key = CmdTempCacheKey.CMD_TEMP_CACHE_KEY_KEY+cmdTemp.getKey();
            redisService.expire(key,0);
            String key1 = CmdTempCacheKey.CMD_TEMP_CACHE_ID_KEY+id;
            redisService.expire(key1,0);
            cmdJsonTempRepository.delete(id);
        }
    }

}
