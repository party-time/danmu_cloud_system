package cn.partytime.repository.manager;

import cn.partytime.model.wechat.WeixinMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by liuwei on 2016/9/23.
 */
public interface WeixinMessageRepository extends MongoRepository<WeixinMessage,String> {

    Page<WeixinMessage> findAll(Pageable pageable);

    Page<WeixinMessage> findByWordsLike(String words, Pageable pageable);

    List<WeixinMessage> findByWords(String words);

}
