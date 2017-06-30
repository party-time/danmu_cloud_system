package cn.partytime.service.param;

import cn.partytime.model.manager.ParamTemplate;
import cn.partytime.repository.manager.param.ParamTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2017/2/23.
 */

@Service
@Slf4j
public class ParamTemplateService {

    @Autowired
    private ParamTemplateRepository paramTemplateRepository;

    public ParamTemplate save(String name){
        ParamTemplate paramTemplate = new ParamTemplate();
        paramTemplate.setName(name);
        return paramTemplateRepository.save(paramTemplate);
    }

    public ParamTemplate update(String id,String name){
        ParamTemplate paramTemplate = paramTemplateRepository.findOne(id);
        paramTemplate.setName(name);
        return paramTemplateRepository.save(paramTemplate);
    }

    public Page<ParamTemplate> findAll(Integer page,Integer size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return paramTemplateRepository.findAll(pageRequest);
    }

    public void del(String id){
        paramTemplateRepository.delete(id);
    }

    public ParamTemplate findById(String id){
        return paramTemplateRepository.findOne(id);
    }

    public List<ParamTemplate> findByIds(List<String> idList){
        return paramTemplateRepository.findByIdIn(idList);
    }

    public List<ParamTemplate> findAll(){
        return paramTemplateRepository.findAll();
    }
}
