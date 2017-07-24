package cn.partytime.rpc;

import cn.partytime.model.monitor.Monitor;
import cn.partytime.repository.manager.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

/**
 * Created by administrator on 2017/7/24.
 */
@RestController
@RequestMapping("/rpcMonitor")
public class RpcMonitorService {

    @Autowired
    private MonitorRepository monitorRepository;

    @RequestMapping(value = "/save" ,method = RequestMethod.POST)
    public Monitor save(@RequestBody Monitor monitor){
        return monitorRepository.insert(monitor);
    }

    @RequestMapping(value = "/update" ,method = RequestMethod.POST)
    public Monitor update(@RequestBody Monitor monitor){
        return monitorRepository.save(monitor);
    }

    @RequestMapping(value = "/findAll" ,method = RequestMethod.GET)
    public Page<Monitor> findAll(@RequestParam int pageNum , @RequestParam int pageSize){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNum, pageSize, sort);
        return monitorRepository.findAll(pageRequest);
    }

    @RequestMapping(value = "/countByKey" ,method = RequestMethod.GET)
    public Integer countByKey(@RequestParam String key){
        return monitorRepository.countByKey(key);
    }

    @RequestMapping(value = "/findById" ,method = RequestMethod.GET)
    public Monitor findById(@RequestParam String id){
        return monitorRepository.findOne(id);
    }

    @RequestMapping(value = "/findByKey" ,method = RequestMethod.GET)
    public Monitor findByKey(@RequestParam String key){
        return monitorRepository.findByKey(key);
    }
}
