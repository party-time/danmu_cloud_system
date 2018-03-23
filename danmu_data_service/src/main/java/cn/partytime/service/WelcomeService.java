package cn.partytime.service;

import cn.partytime.model.welcome.Welcome;
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

    public Welcome findByRandom(){
        List<Welcome> welcomeList = welcomeRepository.findAll();
        Random r = new Random(new Date().getTime());
        int a = r.nextInt(welcomeList.size());
        return welcomeList.get(a);
    }

}
