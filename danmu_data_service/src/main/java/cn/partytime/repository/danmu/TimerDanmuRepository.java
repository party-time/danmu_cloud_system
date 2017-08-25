package cn.partytime.repository.danmu;

import cn.partytime.model.danmu.TimerDanmu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by lENOVO on 2016/10/25.
 */

@EnableMongoRepositories(mongoTemplateRef = "danmuMongoTemplate")
public interface TimerDanmuRepository extends MongoRepository<TimerDanmu, String> {

    public Page<TimerDanmu> findByPartyId(String partyId, Pageable pageable);

    List<TimerDanmu> findByPartyId(String partyId);


    long countByPartyIdLessThanAndBeginTime(long time);

}
