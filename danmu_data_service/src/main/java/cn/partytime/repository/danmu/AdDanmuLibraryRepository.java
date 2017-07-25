package cn.partytime.repository.danmu;

import cn.partytime.model.danmu.AdDanmuLibrary;
import cn.partytime.model.danmu.AdTimerDanmu;
import cn.partytime.model.manager.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */
@EnableMongoRepositories(mongoTemplateRef = "danmuMongoTemplate")
public interface AdDanmuLibraryRepository  extends MongoRepository<AdDanmuLibrary,String> {

    AdDanmuLibrary findById(String id);

    List<AdDanmuLibrary> findByIdIn(List<String> idlist);

    AdDanmuLibrary findByName(String name);

    Page<AdDanmuLibrary> findByIsDeleteLessThanEqual(int isDelete, Pageable pageable);

}
