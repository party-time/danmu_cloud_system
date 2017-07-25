package cn.partytime.repository.manager;

import cn.partytime.model.manager.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Date;
import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface PartyRepository extends MongoRepository<Party,String> {

    Party findById(String id);

    Party findByShortName(String shortName);

    List<Party> findByEndTimeGreaterThan(Date date);

    List<Party> findByNameLike(String name);

    List<Party> findByIdIn(List<String> ids);

    List<Party> findByStartTimeBetweenAndStatusAndType(Date fromDate,Date toDate,Integer status,Integer type);

    List<Party> findByStartTimeBetweenAndStatus(Date fromDate,Date toDate,Integer status);

    List<Party> findByStatusAndType(Integer status,Integer type);

    List<Party> findByStartTimeBetween(Date fromDate,Date toDate);
    //List<Party> findByEndTimeGreaterThanEqualANDStartTimeLessThanEqual(Date date);

    Party findByName(String name);


    List<Party> findByIdInAndStatusAndType(List<String> ids,Integer status,Integer type);

    List<Party> findByIdInAndStatusLessThanAndType(List<String> ids,Integer status,Integer type);

    Page<Party> findByType(Integer type, Pageable pageable);

    Page<Party> findByTypeAndStatus(Integer type, Integer status ,Pageable pageable);

    Page<Party> findByStatusGreaterThan(Integer status,Pageable pageable);

    List<Party> findByTypeAndStatusGreaterThan(Integer type ,Integer status);

    Page<Party> findByStatusLessThan(Integer status,Pageable pageable);

    List<Party> findByTypeAndStatusLessThan(Integer type,Integer status);


    List<Party> findByMovieAliasAndStatusNot(String movieAlias,Integer status);

    List<Party> findByTypeAndStatusNot(Integer type,Integer status);

    Party findByMovieAliasAndStatus(String movieAlias,Integer status);

}
