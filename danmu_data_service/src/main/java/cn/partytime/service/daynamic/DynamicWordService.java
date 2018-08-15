package cn.partytime.service.daynamic;


import cn.partytime.model.daynamic.DynamicContentMode;
import cn.partytime.model.daynamic.DynamicWordMode;
import cn.partytime.repository.manager.daynamic.DynamicWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DynamicWordService {

    @Autowired
    private DynamicWordRepository dynamicWordRepository;



    public void delete(String id){
        dynamicWordRepository.delete(id);
    }

    public List<DynamicWordMode> findAll(){
        return dynamicWordRepository.findAll();
    }

    public DynamicWordMode findById(String id){
        return dynamicWordRepository.findOne(id);
    }

    public void insert(DynamicWordMode dynamicWordMode){
        dynamicWordRepository.insert(dynamicWordMode);
    }

    public void update(DynamicWordMode dynamicWordMode){
        dynamicWordRepository.save(dynamicWordMode);
    }
}
