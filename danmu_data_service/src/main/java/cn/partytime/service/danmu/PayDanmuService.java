package cn.partytime.service.danmu;

import cn.partytime.model.danmu.PayDanmu;
import cn.partytime.repository.danmu.PayDanmuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2018/6/7.
 */

@Service
public class PayDanmuService {

    @Autowired
    private PayDanmuRepository payDanmuRepository;


    public PayDanmu findByDanmuId(String danmuId){
        return payDanmuRepository.findByDanmuId(danmuId);
    }

    public void save(PayDanmu payDanmu){
        payDanmuRepository.save(payDanmu);
    }
}
