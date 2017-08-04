package cn.partytime.service;

import cn.partytime.model.manager.LovePay;
import cn.partytime.repository.manager.LovePayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by administrator on 2017/7/25.
 */
@Service
public class LovePayService {

    @Autowired
    private LovePayRepository lovePayRepository;

    public LovePay save(LovePay lovePay){
        return lovePayRepository.insert(lovePay);
    }

    public LovePay update(LovePay lovePay){
        return lovePayRepository.save(lovePay);
    }

    public LovePay findById(String id){
        return lovePayRepository.findOne(id);
    }

    public void updateStatus(String id,Integer status){
        LovePay lovePay = this.findById(id);
        if( null != lovePay){
            lovePay.setStatus(status);
            this.update(lovePay);
        }
    }

    public Page<LovePay> findAll(int page, int pageSize){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, pageSize, sort);
        return lovePayRepository.findAll(pageRequest);
    }

}
