package cn.partytime.service;

import cn.partytime.common.util.ListUtils;
import cn.partytime.model.danmu.DanmuModel;
import cn.partytime.model.temp.BlockKeywordModel;
import cn.partytime.model.temp.HistoryDanmuModel;
import cn.partytime.repository.danmu.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lENOVO on 2016/10/31.
 */

@Service
public class HistoryService {

    @Resource(name = "danmuMongoTemplate")
    private MongoTemplate danmuMongoTemplate;


    public long findHistoryDanmuCount() {
        Query query = new Query();
        long count = danmuMongoTemplate.count(query, HistoryDanmuModel.class);
        return count;
    }

    public List<HistoryDanmuModel> findAllDanmu() {
        /*Query query = new Query();
        query = new Query();
        query.skip(page);
        query.limit(size);
        query.with(new Sort(Sort.Direction.ASC, "createTime"));
        List<HistoryDanmuModel> historyDanmuModelList = danmuMongoTemplate.find(query, HistoryDanmuModel.class);*/
        return danmuMongoTemplate.findAll(HistoryDanmuModel.class);
    }

    public long blockCount(){
        Query query = new Query();
        return danmuMongoTemplate.count(query, BlockKeywordModel.class);
    }
    public List<BlockKeywordModel> findBlockWord(){
        return danmuMongoTemplate.findAll(BlockKeywordModel.class);
    }


}
