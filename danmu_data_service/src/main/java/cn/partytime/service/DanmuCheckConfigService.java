package cn.partytime.service;
import cn.partytime.repository.manager.DanmuCheckConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by user on 16/7/19.
 */

@Service
@Slf4j
public class DanmuCheckConfigService {

    /*@Autowired
    private DanmuCheckConfigRepository danmuCheckConfigRepository;

    public DanmuCheckConfig save(DanmuCheckConfig danmuCheckConfig) {
        return danmuCheckConfigRepository.insert(danmuCheckConfig);
    }

    public DanmuCheckConfig update(DanmuCheckConfig danmuCheckConfig) {
        return danmuCheckConfigRepository.save(danmuCheckConfig);
    }

    public DanmuCheckConfig findDanmuCheckConfigById(String id) {
        return danmuCheckConfigRepository.findById(id);
    }*/

}
