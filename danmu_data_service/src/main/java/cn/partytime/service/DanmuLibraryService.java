package cn.partytime.service;

import cn.partytime.model.danmu.DanmuLibrary;
import cn.partytime.repository.danmu.DanmuLibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
