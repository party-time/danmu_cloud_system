package cn.partytime.service.cms;

import cn.partytime.model.cms.Page;
import cn.partytime.repository.manager.cms.PageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by administrator on 2017/6/29.
 */
@Service
@Slf4j
public class PageService {

    @Autowired
    private PageRepository pageRepository;

    public Page save(Page page){
        return pageRepository.insert(page);
    }

    public Page update(Page page){
        return pageRepository.save(page);
    }

    public Page findById(String id){
        return pageRepository.findOne(id);
    }

    public Page findByUrl(String url){
        return pageRepository.findByUrl(url);
    }

}
