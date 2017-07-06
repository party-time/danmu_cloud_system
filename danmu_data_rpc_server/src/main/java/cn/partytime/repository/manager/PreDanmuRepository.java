package cn.partytime.repository.manager;

import cn.partytime.model.danmu.PreDanmuModel;
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
public interface PreDanmuRepository extends MongoRepository<PreDanmuModel,String> {

    //public PreDanmuModel findByMsg(String msg);

    public List<PreDanmuModel> findByDanmuLibraryId(String danmuLibraryId);

    public List<PreDanmuModel> findByDanmuLibraryId(String danmuLibraryId, Sort sort);

    public Page<PreDanmuModel> findByDanmuLibraryId(String danmuLibraryId, Pageable pageable);

    //public Page<PreDanmuModel> findByDanmuLibraryIdAndMsgLike(String danmuLibraryId,String msg, Pageable pageable);

}
