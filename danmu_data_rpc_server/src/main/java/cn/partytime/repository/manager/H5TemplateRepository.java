package cn.partytime.repository.manager;

import cn.partytime.model.manager.H5Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by administrator on 2017/4/28.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface H5TemplateRepository extends MongoRepository<H5Template,String> {

    public H5Template findByH5Url(String h5Url);

    public Integer countByIsBase(Integer isBase);

    public Integer countByH5Url(String h5Url);

    public Integer countByIsBaseAndIdNot(Integer isBase, String id);

    public Integer countByH5UrlAndIdNot(String h5Url, String id);

    public Page<H5Template> findByIsIndex(Integer isIndex, Pageable pageable);

    public H5Template findByIsBase(Integer isBase);

}
