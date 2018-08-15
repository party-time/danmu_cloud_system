package cn.partytime.service.daynamic;


import cn.partytime.model.daynamic.DynamicContentMode;
import cn.partytime.repository.manager.daynamic.DynamicContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicContentService {

    @Autowired
    private DynamicContentRepository dynamicContentRepository;

    public void delete(String id){
        dynamicContentRepository.delete(id);
    }

    public DynamicContentMode findById(String id){
        return dynamicContentRepository.findOne(id);
    }

    public void insert(DynamicContentMode dynamicContentMode){
        dynamicContentRepository.insert(dynamicContentMode);
    }

    public void update(DynamicContentMode dynamicContentMode){
        dynamicContentRepository.save(dynamicContentMode);
    }

}
