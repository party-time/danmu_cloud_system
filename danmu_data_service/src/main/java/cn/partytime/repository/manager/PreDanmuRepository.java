package cn.partytime.repository.manager;

import cn.partytime.model.danmu.PreDanmu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface PreDanmuRepository extends MongoRepository<PreDanmu,String> {

    //public PreDanmu findByMsg(String msg);

    public List<PreDanmu> findByDanmuLibraryId(String danmuLibraryId);

    public List<PreDanmu> findByDanmuLibraryId(String danmuLibraryId, Sort sort);

    public Page<PreDanmu> findByDanmuLibraryId(String danmuLibraryId, Pageable pageable);

    //public Page<PreDanmu> findByDanmuLibraryIdAndMsgLike(String danmuLibraryId,String msg, Pageable pageable);

}
