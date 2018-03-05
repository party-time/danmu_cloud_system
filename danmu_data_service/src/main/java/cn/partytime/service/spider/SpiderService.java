package cn.partytime.service.spider;

import cn.partytime.model.spider.Spider;
import cn.partytime.repository.manager.spider.SpiderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 2017/12/25.
 */

@Service
@Slf4j
public class SpiderService {

    @Autowired
    private SpiderRepository spiderRepository;

    public void saveOrUpDate(List<Spider> spiderList){
        if( null == spiderList || spiderList.size()==0){
            return;
        }
        for(Spider spider : spiderList){
            if( !StringUtils.isEmpty(spider.getOldId())){
                Spider spider1 = spiderRepository.findByOldId(spider.getOldId());
                if( null == spider1){
                    if(!StringUtils.isEmpty(spider.getDateStr())){
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date =formatter.parse(spider.getDateStr());
                            spider.setDate(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    spiderRepository.insert(spider);
                }else{
                    spider1.setOldId(spider.getOldId());
                    if(!StringUtils.isEmpty(spider.getName())){
                        spider1.setName(spider.getName());
                    }
                    if( null != spider.getSource()){
                        spider1.setSource(spider.getSource());
                    }
                    if( !StringUtils.isEmpty(spider.getImageUrl())){
                        spider1.setImageUrl(spider.getImageUrl());
                    }
                    if( null != spider.getStatus()){
                        spider1.setStatus(spider.getStatus());
                    }
                    if( null != spider.getTime()){
                        spider1.setTime(spider.getTime());
                    }
                    if(!StringUtils.isEmpty(spider.getDateStr())){
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date =formatter.parse(spider.getDateStr());
                            spider1.setDate(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if( null != spider.getScore()){
                        spider1.setScore(spider.getScore());
                    }
                    if( null != spider.getTypeList() && spider.getTypeList().size() > 0){
                        spider1.setTypeList(spider.getTypeList());
                    }
                    spiderRepository.save(spider1);
                }
            }
        }
    }


    public Page<Spider> findAll(Integer page , Integer size){
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return spiderRepository.findAll(pageRequest);
    }

    public Spider findById(String id){
        return spiderRepository.findOne(id);
    }

    public void update(Spider spider){
        if( null != spider){
            Spider spider1 = spiderRepository.findOne(spider.getId());
            if( null != spider1){
                spider1.setName(spider.getName());
                spider1.setDateStr(spider.getDateStr());
                spider1.setScore(spider.getScore());
                spider1.setTime(spider.getTime());
                if(!StringUtils.isEmpty(spider.getDateStr())){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date =formatter.parse(spider.getDateStr());
                        spider1.setDate(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                spiderRepository.save(spider1);
            }

        }
    }

    public List<Spider> findByDateGreaterThan(Date date){
        return spiderRepository.findByDateGreaterThanAndPartyIdIsNull(date);
    }

    public void updatePartyId(String id,String partyId){
        Spider spider = spiderRepository.findOne(id);
        if( null != spider){
            spider.setPartyId(partyId);
            spiderRepository.save(spider);
        }
    }
}
