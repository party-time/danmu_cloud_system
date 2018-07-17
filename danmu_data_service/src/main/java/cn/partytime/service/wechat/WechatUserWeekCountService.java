package cn.partytime.service.wechat;


import cn.partytime.model.wechat.WechatUserWeekCount;
import cn.partytime.repository.user.WechatUserWeekCountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class WechatUserWeekCountService {

    @Autowired
    private WechatUserWeekCountRepository wechatUserWeekCountRepository;

    public Page<WechatUserWeekCount> findAll(Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return wechatUserWeekCountRepository.findAll(pageRequest);
    }

    public WechatUserWeekCount findByAddressIdAndStartDateAndEndDate(String addressId,Date startDate, Date endDate){
        return  wechatUserWeekCountRepository.findByAddressIdAndStartDateAndEndDate(addressId,startDate,endDate);
    }

    public void save(WechatUserWeekCount wechatUserWeekCount){
        wechatUserWeekCountRepository.save(wechatUserWeekCount);
    }

}
