package cn.partytime.repository.manager;

import cn.partytime.model.manager.FastDanmu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface FastDanmuRepository extends MongoRepository<FastDanmu,String> {

    FastDanmu findByWord(String word);

    Page<FastDanmu> findByWordLike(String word, Pageable pageable);

    Page<FastDanmu> findByPartyId(String partyId,Pageable pageable);

    FastDanmu findByWordAndPartyId(String word,String partyId);

}
