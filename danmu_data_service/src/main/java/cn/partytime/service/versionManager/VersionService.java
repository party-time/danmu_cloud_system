package cn.partytime.service.versionManager;

import cn.partytime.model.manager.Version;
import cn.partytime.repository.manager.versionManager.VersionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2017/2/13.
 */
@Service
@Slf4j
public class VersionService {

    @Autowired
    private VersionRepository versionRepository;



    public Version save(String name, String versionNum, String describe, Integer type){
        Version version = new Version();
        version.setName(name);
        version.setVersion(versionNum);
        version.setDescribe(describe);
        version.setType(type);
        return versionRepository.insert(version);
    }

    public void del(String id){
        versionRepository.delete(id);
    }

    public Page<Version> findAll(int page, int size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return versionRepository.findAll(pageRequest);
    }

    public List<Version> findByVersionAndType(String version, Integer type){
        return versionRepository.findByVersionAndType(version,type);
    }


    public List<Version> findByIds(List<String> idList){
        return versionRepository.findByIdIn(idList);
    }

    public Version findById(String id){
        return versionRepository.findOne(id);
    }

    public Page<Version> findByIdNotIn(List<String> idList, int page, int size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return versionRepository.findByIdNotIn(idList,pageRequest);
    }
}
