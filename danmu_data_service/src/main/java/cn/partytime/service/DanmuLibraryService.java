package cn.partytime.service;

import cn.partytime.model.danmu.DanmuLibrary;
import cn.partytime.model.manager.Party;
import cn.partytime.repository.danmu.DanmuLibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by liuwei on 2016/10/21.
 */
@Service
public class DanmuLibraryService {

    @Autowired
    private DanmuLibraryRepository danmuLibraryRepository;

    public Page<DanmuLibrary> findByName(String name,int page, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, pageSize, sort);
        if(StringUtils.isEmpty(name)){
            return danmuLibraryRepository.findAll(pageRequest);
        }
        return danmuLibraryRepository.findByNameLike(name,pageRequest);
    }

    public DanmuLibrary findById(String id){
        return danmuLibraryRepository.findOne(id);
    }

    public DanmuLibrary save(String name){
        if(StringUtils.isEmpty(name)){
            throw new IllegalArgumentException("创建弹幕库名称过长");
        }
        DanmuLibrary danmulibrary = new DanmuLibrary();
        danmulibrary.setName(name);
        danmuLibraryRepository.insert(danmulibrary);
        return danmulibrary;
    }

    public List<DanmuLibrary> findAll(){
        return danmuLibraryRepository.findAll();
    }


}
