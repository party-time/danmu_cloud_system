package cn.partytime.service;

import cn.partytime.common.cachekey.wechat.WelcomeCacheKey;
import cn.partytime.model.welcome.Welcome;
import cn.partytime.redis.service.RedisService;
import cn.partytime.repository.manager.WelcomeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class WelcomeService {

    @Autowired
    private WelcomeRepository welcomeRepository;

    @Autowired
    private RedisService redisService;

    public void delete(String id) {
        welcomeRepository.delete(id);
    }

    public Welcome findByWord(String word) {
        if (StringUtils.isEmpty(word)) {
            return null;
        }
        return welcomeRepository.findByMessage(word);
    }

    public Welcome save(Welcome welcome) {
        return welcomeRepository.insert(welcome);
    }

    public List<Welcome> findAll() {
        return welcomeRepository.findAll();
    }

    public Page<Welcome> findAll(int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return welcomeRepository.findAll(pageRequest);
    }

    public Page<Welcome> findByWordLike(String word , int page , int size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return welcomeRepository.findByMessageLike(word,pageRequest);
    }

    public Welcome findByRandom(String openId){
        Object obj = redisService.get(WelcomeCacheKey.WELCOME_KEY+openId);
        if( null == obj){
            List<Welcome> welcomeList = welcomeRepository.findAll();
            if( null != welcomeList && welcomeList.size()>0){
                Random r = new Random(new Date().getTime());
                int a = r.nextInt(welcomeList.size());
                redisService.set(WelcomeCacheKey.WELCOME_KEY+openId,"",60*10);
                return welcomeList.get(a);
            }
        }
        return null;

    }

}
