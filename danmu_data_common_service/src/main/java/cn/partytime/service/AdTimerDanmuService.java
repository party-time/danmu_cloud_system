package cn.partytime.service;

import cn.partytime.model.danmu.AdTimerDanmu;
import cn.partytime.repository.danmu.AdTimerDanmuRepository;
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
public class AdTimerDanmuService {

    @Autowired
    private AdTimerDanmuRepository adTimerDanmuRepository;


    public void save(AdTimerDanmu adTimerDanmu){
        adTimerDanmuRepository.save(adTimerDanmu);
    }

    public void insertAdTimerDanmu(AdTimerDanmu adTimerDanmu){
        adTimerDanmuRepository.insert(adTimerDanmu);
    }

    public AdTimerDanmu findAdTimerDanmu(String id){
        return adTimerDanmuRepository.findById(id);
    }

    public void updateAdTimerDanmu(AdTimerDanmu adTimerDanmu){
        adTimerDanmuRepository.save(adTimerDanmu);
    }

    public void deleteAdTimerDanmu(String id){
        adTimerDanmuRepository.delete(id);
    }


    public void deleteByLibraryId(String libraryId){
        adTimerDanmuRepository.deleteByLibraryId(libraryId);
    }

    public List<AdTimerDanmu> findByLibraryIdAllTimerDanmu(String libraryId) {
        return adTimerDanmuRepository.findByLibraryIdOrderByBeginTimeAsc(libraryId);
    }

    public List<AdTimerDanmu> findByPoolIdIn(List<String> poolIdList ) {
        return adTimerDanmuRepository.findByLibraryIdIn(poolIdList);
    }


    public Page<AdTimerDanmu> findByLibraryId(String libraryId, int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return adTimerDanmuRepository.findByLibraryId(libraryId,pageRequest);
    }
}
