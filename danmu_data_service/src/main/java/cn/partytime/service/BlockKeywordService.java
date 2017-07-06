package cn.partytime.service;

import cn.partytime.model.manager.BlockKeyword;
import cn.partytime.repository.manager.BlockKeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */

@Service("blockKeywordService")
@Slf4j
public class BlockKeywordService {

    @Resource(name = "managerMongoTemplate")
    private MongoTemplate managerMongoTemplate;

    @Autowired
    private BlockKeywordRepository blockKeywordRepository;

    public void delete(String id) {
        blockKeywordRepository.delete(id);
    }

    public BlockKeyword findByWord(String word) {
        if (StringUtils.isEmpty(word)) {
            return null;
        }
        return blockKeywordRepository.findByWord(word);
    }

    public BlockKeyword save(BlockKeyword blockKeyword) {
        return blockKeywordRepository.insert(blockKeyword);
    }

    public List<BlockKeyword> findAll() {
        //return blockKeywordRepository.findAll();
        Query query = new Query();
        query.with(new Sort(Sort.Direction.ASC, "word"));
        return managerMongoTemplate.find(query, BlockKeyword.class);
    }

    public Page<BlockKeyword> findAll(int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return blockKeywordRepository.findAll(pageRequest);
    }

    public Page<BlockKeyword> findByWordLike(String word , int page , int size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return blockKeywordRepository.findByWordLike(word,pageRequest);
    }


}
