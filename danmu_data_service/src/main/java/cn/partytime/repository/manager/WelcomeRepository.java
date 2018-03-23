package cn.partytime.repository.manager;

import cn.partytime.model.welcome.Welcome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WelcomeRepository extends MongoRepository<Welcome,String> {

    Welcome findByMessage(String message);

    Page<Welcome> findByMessageLike(String message, Pageable pageable);
}
