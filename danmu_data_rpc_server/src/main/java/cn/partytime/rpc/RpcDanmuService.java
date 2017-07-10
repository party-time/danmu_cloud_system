package cn.partytime.rpc;

import cn.partytime.model.danmu.DanmuLog;
import cn.partytime.model.danmu.DanmuModel;
import cn.partytime.repository.danmu.DanmuRepository;
import cn.partytime.service.DanmuLogService;
import cn.partytime.service.DanmuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dm on 2017/7/10.
 */

@RestController
@RequestMapping("/rpcDanmu")
public class RpcDanmuService {



    @Autowired
    private DanmuLogService danmuLogService;


    @Autowired
    private DanmuService danmuService;

    @Autowired
    private DanmuRepository danmuRepository;

    @RequestMapping(value = "/danmuLogSave" ,method = RequestMethod.POST)
    public DanmuLog danmuLogSave(@RequestBody DanmuLog danmuLog){
        return  danmuLogService.save(danmuLog);
    }


    @RequestMapping(value = "/findDanmuLogById" ,method = RequestMethod.GET)
    public DanmuLog findDanmuLogById(@RequestParam String id){
        return  danmuLogService.findDanmuLogById(id);
    }

    @RequestMapping(value = "/danmuSave" ,method = RequestMethod.POST)
    public DanmuModel danmuSave(@RequestBody  DanmuModel danmuModel) {
        return danmuService.save(danmuModel);
    }


    @RequestMapping(value = "/findDanmuById" ,method = RequestMethod.GET)
    public DanmuModel findById(@RequestParam String id) {
        return danmuService.findById(id);
    }

    @RequestMapping(value = "/findDanmuByIsBlocked" ,method = RequestMethod.GET)
    public List<DanmuModel> findDanmuByIsBlocked(@RequestParam int page, @RequestParam int size, @RequestParam boolean isBlocked){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        Page<DanmuModel> danmuModelPage = danmuRepository.findByIsBlocked(isBlocked,pageRequest);
        return danmuModelPage.getContent();
    }

}
