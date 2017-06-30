package cn.partytime.service;

import cn.partytime.common.util.ListUtils;
import cn.partytime.model.client.DanmuClient;
import cn.partytime.model.manager.PartyResource;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.model.manager.TimerDanmuFile;
import cn.partytime.repository.manager.TimerDanmuFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lENOVO on 2016/11/28.
 */

@Service
public class TimerDanmuFileService {

    @Autowired
    private TimerDanmuFileRepository timerDanmuFileRepository;


    @Resource(name = "managerMongoTemplate")
    private MongoTemplate managerMongoTemplate;

    /**
     * 根据活动编号获取所有资源
     *
     * @param partyIdList
     * @return
     */
    public List<TimerDanmuFile> findByPartyId(List<String> partyIdList) {

        Criteria criteria = new Criteria().andOperator(
                Criteria.where("partyId").in(partyIdList)
        );
        Query query = new Query();
        query.addCriteria(criteria);
        return managerMongoTemplate.find(query, TimerDanmuFile.class);
    }


    /**
     * 删除活动下所有旧数据
     * @param partyId
     */
    public void deleteOldDataByPartyId(String partyId){
        List<TimerDanmuFile> timerDanmuFileList =  timerDanmuFileRepository.findByPartyId(partyId);
        if(ListUtils.checkListIsNotNull(timerDanmuFileList)){
            for(TimerDanmuFile timerDanmuFile:timerDanmuFileList){
                timerDanmuFileRepository.delete(timerDanmuFile.getId());
            }
        }
    }

    public List<TimerDanmuFile> findByPartyId(String partyId){
        return timerDanmuFileRepository.findByPartyId(partyId);
    }


    public void save(TimerDanmuFile timerDanmuFile){
        timerDanmuFileRepository.save(timerDanmuFile);
    }


}
