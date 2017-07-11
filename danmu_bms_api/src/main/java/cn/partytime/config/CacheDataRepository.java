package cn.partytime.config;

import cn.partytime.model.BlockKeyQeueueModel;
import cn.partytime.model.manager.BlockKeyword;
import cn.partytime.repository.manager.BlockKeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lENOVO on 2016/8/24.
 */

@Component
public class CacheDataRepository {

    @Autowired
    private BlockKeywordRepository blockKeywordRepository;

    //使用缓存的编号
    private volatile int cacheNo = 0;
    //缓存A
    private volatile  Map<String,String> blockKeywordAMap = new HashMap<String,String>();
    //缓存B
    private volatile  Map<String,String> blockKeywordBMap = new HashMap<String,String>();



    @PostConstruct
    void initCache() {
        //加载敏感词
        List<BlockKeyword> blockKeyQeueueModelList = blockKeywordRepository.findAll();
        for (BlockKeyword blockKeyword : blockKeyQeueueModelList) {
            blockKeywordAMap.put(blockKeyword.getId(),blockKeyword.getWord());
            blockKeywordBMap.put(blockKeyword.getId(),blockKeyword.getWord());
        }
    }

    private void updateCache(Map<String, String> cache,BlockKeyQeueueModel blockKeyQeueueModel){
        if (blockKeyQeueueModel.getStatus() != 2) {
            cache.put(blockKeyQeueueModel.getId(), blockKeyQeueueModel.getWord());
        } else {
            cache.remove(blockKeyQeueueModel.getId());
        }
    }

    /**
     * 批量更新
     *
     * @param blockKeyQeueueModelList
     */
    public void updateCacheList(List<BlockKeyQeueueModel> blockKeyQeueueModelList) {
        if (cacheNo == 0) {
            //A队列正在使用。更新B队列
            for (BlockKeyQeueueModel blockKeyQeueueModel : blockKeyQeueueModelList) {
                updateCache(blockKeywordBMap, blockKeyQeueueModel);
            }
            cacheNo = 1;
            blockKeywordAMap.clear();
            blockKeywordAMap.putAll(blockKeywordBMap);
        } else {
            //A队列正在使用。更新B队列
            for (BlockKeyQeueueModel blockKeyQeueueModel : blockKeyQeueueModelList) {
                updateCache(blockKeywordAMap, blockKeyQeueueModel);
            }
            cacheNo = 0;
            blockKeywordBMap.clear();
            blockKeywordBMap.putAll(blockKeywordAMap);
        }
    }

    /**
     * 单个更新敏感词队列
     *
     * @param blockKeyQeueueModel
     */
    public void updateCacheOne(BlockKeyQeueueModel blockKeyQeueueModel) {
        if (cacheNo == 0) {
            //A队列正在使用。更新B队列
            updateCache(blockKeywordBMap, blockKeyQeueueModel);
            cacheNo = 1;
            blockKeywordAMap.clear();
            blockKeywordAMap.putAll(blockKeywordBMap);
        } else {
            //A队列正在使用。更新B队列
            updateCache(blockKeywordAMap, blockKeyQeueueModel);
            cacheNo = 0;
            blockKeywordBMap.clear();
            blockKeywordBMap.putAll(blockKeywordAMap);
        }
    }


    /**
     * 匹配敏感词
     *
     * @param word 敏感词
     * @return
     */
    public boolean matchBlockKey(String word) {
        if (cacheNo == 0) {
            return matchBlockKey(blockKeywordAMap, word.toLowerCase());
        } else {
            return matchBlockKey(blockKeywordAMap, word.toLowerCase());
        }
    }

    boolean matchBlockKey(Map<String, String> map, String word) {
        if (map.size() == 0) {
            return false;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (word.contains(entry.getValue().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
