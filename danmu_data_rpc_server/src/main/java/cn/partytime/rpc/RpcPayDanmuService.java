package cn.partytime.rpc;

import cn.partytime.model.danmu.Danmu;
import cn.partytime.model.danmu.PayDanmu;
import cn.partytime.service.danmu.PayDanmuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by admin on 2018/6/7.
 */

@Slf4j
@RestController
@RequestMapping("/rpcPayDanmu")
public class RpcPayDanmuService {


    @Autowired
    private PayDanmuService payDanmuService;

    @RequestMapping(value = "/findByDanmuId" ,method = RequestMethod.POST)
    public PayDanmu findByDanmuId(@RequestParam String danmuId) {
        return payDanmuService.findByDanmuId(danmuId);
    }


    @RequestMapping(value = "/save" ,method = RequestMethod.GET)
    public void save(@RequestBody PayDanmu payDanmu) {
        payDanmuService.save(payDanmu);
    }
}
