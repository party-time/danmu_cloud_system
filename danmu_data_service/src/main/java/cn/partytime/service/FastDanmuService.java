package cn.partytime.service;

import cn.partytime.model.manager.FastDanmu;
import cn.partytime.repository.manager.FastDanmuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class FastDanmuService {

    @Autowired
    private FastDanmuRepository fastDanmuRepository;

    public void delete(String id) {
        fastDanmuRepository.delete(id);
    }

    public FastDanmu findByWord(String word) {
        if (StringUtils.isEmpty(word)) {
            return null;
        }
        return fastDanmuRepository.findByWord(word);
    }

    public FastDanmu findByPartyIdAndWord(String partyId,String word){
        return fastDanmuRepository.findByWordAndPartyId(word,partyId);
    }

    public FastDanmu save(FastDanmu fastDanmu) {
        return fastDanmuRepository.insert(fastDanmu);
    }


    public Page<FastDanmu> findAll(int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return fastDanmuRepository.findAll(pageRequest);
    }

    public Page<FastDanmu> findByPartyId(String partyId,int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return fastDanmuRepository.findByPartyId(partyId,pageRequest);
    }

    public Page<FastDanmu> findByWordLike(String word , int page , int size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return fastDanmuRepository.findByWordLike(word,pageRequest);
    }
}
