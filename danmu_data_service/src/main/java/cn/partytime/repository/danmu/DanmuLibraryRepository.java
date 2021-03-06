package cn.partytime.repository.danmu;

import cn.partytime.model.danmu.DanmuLibrary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Date;
import java.util.List;

/**
 * Created by liuwei on 2016/10/21.
 */
@EnableMongoRepositories(mongoTemplateRef = "danmuMongoTemplate")
public interface DanmuLibraryRepository  extends MongoRepository<DanmuLibrary,String> {

    List<DanmuLibrary> findByNameLike(String name);

    DanmuLibrary findByName(String name);

    List<DanmuLibrary> findByIdIn(List<String> ids);

    List<DanmuLibrary> findByIdNotIn(List<String> ids);

    Page<DanmuLibrary> findByNameLike(String name, Pageable pageable);

    Page<DanmuLibrary> findAll(Pageable pageable);
}

