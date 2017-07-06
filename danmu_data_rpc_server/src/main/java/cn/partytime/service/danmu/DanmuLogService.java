package cn.partytime.service.danmu;

import cn.partytime.model.danmu.DanmuLog;
import cn.partytime.repository.danmu.DanmuLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/5/16.
 */

@Controller
@RequestMapping("/danmuLog")
public class DanmuLogService {


    @Autowired
    private DanmuLogRepository danmuLogRepository;


    public DanmuLog save(DanmuLog danmuLog){
        return  danmuLogRepository.save(danmuLog);
    }


    @RequestMapping(value = "/findDanmuLogById" ,method = RequestMethod.GET)
    public DanmuLog findDanmuLogById(@RequestParam String id){
        return  danmuLogRepository.findOne(id);
    }



    public Page<DanmuLog> findDanmuByIsBlocked(int page, int size, boolean isBlocked){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuLogRepository.findByIsBlocked(isBlocked,pageRequest);
    }

}
