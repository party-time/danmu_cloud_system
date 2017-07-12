package cn.partytime.service.adDanmu;

import cn.partytime.model.danmu.AdDanmuLibrary;
import cn.partytime.repository.danmu.AdDanmuLibraryRepository;
import cn.partytime.service.AdTimerDanmuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */
@Service
public class AdDanmuLibraryService {

    @Autowired
    private AdDanmuLibraryRepository adDanmuLibraryRepository;

    @Autowired
    private AdTimerDanmuService adTimerDanmuService;


    public void updateAdDanmuLibrary(AdDanmuLibrary adDanmuLibrary){
        adDanmuLibraryRepository.save(adDanmuLibrary);
    }


    public void insertAdDanmuLibrary(AdDanmuLibrary adDanmuLibrary){
        adDanmuLibraryRepository.insert(adDanmuLibrary);
    }

    public AdDanmuLibrary findAdDanmuLibraryById(String id){
       return adDanmuLibraryRepository.findById(id);
    }

    public List<AdDanmuLibrary> findAdDanmuLibraryByIdList(List<String> idList){
        return adDanmuLibraryRepository.findByIdIn(idList);
    }

    public void deleteAdDanmuLibrary(String id){
        AdDanmuLibrary adDanmuLibrary = findAdDanmuLibraryById(id);
        if(adDanmuLibrary!=null){
            adTimerDanmuService.deleteByLibraryId(adDanmuLibrary.getId());
        }
    }

    public Page<AdDanmuLibrary> findByIsDeleteLessThan(int page, int size, int isDelete){
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return  adDanmuLibraryRepository.findByIsDeleteLessThanEqual(isDelete,pageRequest);
    }

    public AdDanmuLibrary findByName(String name){
        return adDanmuLibraryRepository.findByName(name);
    }


}
