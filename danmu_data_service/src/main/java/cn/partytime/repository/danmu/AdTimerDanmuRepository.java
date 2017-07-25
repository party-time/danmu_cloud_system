package cn.partytime.repository.danmu;

import cn.partytime.model.danmu.AdTimerDanmu;
import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.model.manager.DanmuAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */
@EnableMongoRepositories(mongoTemplateRef = "danmuMongoTemplate")
public interface AdTimerDanmuRepository extends MongoRepository<AdTimerDanmu,String> {

    Page<AdTimerDanmu> findByLibraryId(String libraryId , Pageable pageable);

    List<AdTimerDanmu> findByLibraryIdOrderByBeginTimeAsc(String libraryId);

    List<AdTimerDanmu> findByLibraryIdIn(List<String> libraryIdList);

    AdTimerDanmu findById(String id);

    long deleteByLibraryId(String libraryId);
}
