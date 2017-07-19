package cn.partytime.service.timerDanmu;

import cn.partytime.model.danmu.TimerDanmu;
import cn.partytime.repository.danmu.TimerDanmuRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lENOVO on 2016/10/25.
 */
@Service
public class TimerDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(TimerDanmuService.class);

    @Resource(name = "danmuMongoTemplate")
    private MongoTemplate danmuMongoTemplate;

    @Autowired
    public TimerDanmuRepository timerDanmuRepository;


    public void save(TimerDanmu timerDanmu) {
        timerDanmuRepository.save(timerDanmu);
    }

    public void delete(String id) {
        timerDanmuRepository.delete(id);
    }


    public List<TimerDanmu> findDanmuAll(String partyId) {
        return timerDanmuRepository.findByPartyId(partyId);
    }



    public Page<TimerDanmu> findByPartyId(int page, int size, String partyId) {
        Sort sort = new Sort(Sort.Direction.ASC, "beginTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return timerDanmuRepository.findByPartyId(partyId, pageRequest);
    }

    public TimerDanmu findTimerDanmu(String id){
        return  timerDanmuRepository.findOne(id);
    }

    public Page<TimerDanmu> findByPartyIdOrderBytime(int page, int size, String partyId) {
        Sort sort = new Sort(Sort.Direction.ASC, "beginTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return timerDanmuRepository.findByPartyId(partyId, pageRequest);
    }


    public long findCountByPartyId(String partyId){
        Query query = Query.query(Criteria.where("partyId").is(partyId));
        return danmuMongoTemplate.count(query,TimerDanmu.class);
    }



    /**
     * 通过活动编号分组查询
     * @param partyId
     * @return
     */
    public DBObject timerDanmuCountGroupByPartyId(String partyId) {
        String reduce = "function(doc, aggr){    aggr.count += 1;       }";
        Query query = Query.query(Criteria.where("partyId").is(partyId));
        DBObject dbObject = danmuMongoTemplate.getCollection("timer_danmu").group(new BasicDBObject("beginTime", 1), query.getQueryObject(), new BasicDBObject("count", 0), reduce);
        return dbObject;
    }
}
