package cn.partytime.service;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.danmu.Danmu;
import cn.partytime.model.danmu.PreDanmu;
import cn.partytime.repository.danmu.DanmuRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwei on 16/6/15.
 */

@Service
@Slf4j
public class DanmuService {

    @Resource(name = "danmuMongoTemplate")
    private MongoTemplate danmuMongoTemplate;

    @Autowired
    private DanmuRepository danmuRepository;

    public Danmu save(Danmu danmuModel) {
        return danmuRepository.save(danmuModel);
    }



    public void deleteById(String id) {
        danmuRepository.delete(id);
    }

    public Danmu findById(String id) {
        return danmuRepository.findById(id);
    }

    public List<Danmu> findByDanmuPoolId(String danmuPoolId) {
        return danmuRepository.findByDanmuPoolId(danmuPoolId);
    }

    public Page<Danmu> findDanmuListByPage(int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuRepository.findAll(pageRequest);
    }

    public Page<Danmu> findDanmuByIsBlocked(int page, int size, boolean isBlocked){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuRepository.findByIsBlocked(isBlocked,pageRequest);
    }


    public PageResultModel<Danmu> findByMsgLike(String msg, int page, int size){
        /*Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuRepository.findByMsgLike(msg,pageRequest);*/
        Criteria criteria = new Criteria();
        //criteria.where("content.message").regex("/^111*/");
        criteria.andOperator(Criteria.where("content.message").regex(".*?" + msg + ".*"));
        Map<String, Object> result = new HashMap<>();
        Query query = new Query(criteria);
        query.skip(page * size);
        query.limit(size);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        List<Danmu> list = this.danmuMongoTemplate.find(query, Danmu.class);
        long count = this.danmuMongoTemplate.count(query, PreDanmu.class);

        //pageResultModel<PreDanmu>
        //result.put("datas", list);
        //result.put("size", count);

        PageResultModel<Danmu> preDanmuModelPageResultModel = new PageResultModel<>();
        preDanmuModelPageResultModel.setTotal(count);
        preDanmuModelPageResultModel.setRows(list);
        return preDanmuModelPageResultModel;
    }


    public List<Danmu> findByBlockedAndViewFlgAndDanmuPoolIdInOrderByTimeDesc(String templateIdKey,List<String> poolIdList){
        return danmuRepository.findByIsBlockedAndViewFlgAndDanmuSrcAndTemplateIdKeyAndDanmuPoolIdInOrderByTimeDesc(false,true,1,templateIdKey,poolIdList);
    }



    public List<Danmu> findDanmuListByPartyIdAndTimeAndDanmuPool(String partyId, int time, List<String>danmuPoolIdList, int limit){
        Query query = new Query();
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("danmuPoolId").in(danmuPoolIdList),
                Criteria.where("time").gte(time),
                Criteria.where("isBlocked").is(false),
                Criteria.where("viewFlg").is(true)
        );
        Sort sort = new Sort(Sort.Direction.ASC, "time");
        query.addCriteria(criteria);
        query.with(sort).skip(0).limit(limit);
        return danmuMongoTemplate.find(query,Danmu.class);
    }




    public Page<Danmu> findByDanmuPoolIdAndDanmuSrc(int page, int size, String danmuPoolId, boolean isBlocked, int danmuSrc) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuRepository.findByDanmuPoolIdAndDanmuSrcAndIsBlocked(danmuPoolId,danmuSrc,isBlocked,pageRequest);
    }


    public PageResultModel<Danmu> findDanmuByDanmuSrcAndIsBlockedAndViewFlgAndUpdateTimeBetween(int danmuSrc, boolean isBlocked, boolean viewFlg, Integer page, Integer size,Date startDate,Date endDate){
        /*Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        //return danmuRepository.findDanmuByDanmuSrcAndIsBlockedAndViewFlgAndUpdateTimeBetween(danmuSrc,isBlocked,viewFlg,startDate,endDate,pageRequest);
        return danmuRepository.findDanmuByUpdateTimeBetween(startDate,endDate,pageRequest);*/
        //criteria.where("content.message").regex("/^111*/");
        //criteria.andOperator(Criteria.where("content.message").regex(".*?" + msg + ".*"),Criteria.where("danmuLibraryId").is(dlId));
        Criteria criteria = new Criteria().andOperator(
                /*Criteria.where("danmuSrc").is(danmuSrc),
                Criteria.where("isBlocked").is(isBlocked),
                Criteria.where("viewFlg").is(viewFlg),*/
                Criteria.where("updateTime").gte(startDate).lte(endDate)
        );

        Map<String, Object> result = new HashMap<>();
        Query query = new Query(criteria);
        query.skip(page * size);
        query.limit(size);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));

        List<Danmu> list = this.danmuMongoTemplate.find(query, Danmu.class);
        log.info("list:{}",JSON.toJSONString(list));

        long count = this.danmuMongoTemplate.count(query, Danmu.class);
        log.info("count:{}",count);

        PageResultModel<Danmu> danmuPageResultModel = new PageResultModel<>();
        danmuPageResultModel.setTotal(count);
        danmuPageResultModel.setRows(list);
        return danmuPageResultModel;

    }

    public Page<Danmu> findByDanmuSrcAndIsBlockedAndViewFlgAndDanmuPoolIdWithin(int danmuSrc, boolean isBlocked, boolean viewFlg, Integer page, Integer size, List<String> danmuPoolIdList) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuRepository.findByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlg(danmuPoolIdList,danmuSrc,isBlocked,viewFlg,pageRequest);
    }

    public Page<Danmu> findByDanmuSrcAndIsBlockedAndViewFlgAndDanmuPoolIdWithin(int danmuSrc, boolean isBlocked, boolean viewFlg, Integer page, Integer size, List<String> danmuPoolIdList,String templateIdKey) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuRepository.findByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlgAndTemplateIdKey(danmuPoolIdList,danmuSrc,isBlocked,viewFlg,pageRequest,templateIdKey);
    }


    public Page<Danmu> findByDanmuPoolIdAndDanmuSrcAndIsBlockedAndViewFlg(String danmuPoolId, int danmuSrc, boolean isBlocked, boolean viewFlg, Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuRepository.findByDanmuPoolIdAndDanmuSrcAndIsBlockedAndViewFlg(danmuPoolId,danmuSrc,isBlocked,viewFlg,pageRequest);
    }


    public long countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlg(List<String> danmuPoolIdList,int danmuSrc,boolean isBlocked,boolean viewFlg){
        return danmuRepository.countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlg(danmuPoolIdList,danmuSrc,isBlocked,viewFlg);
    }


    public List<Danmu> findDanmuByIdList(List<String> idList){
        return danmuRepository.findByIdIn(idList);
    }

    /**
     * 屏蔽弹幕
     * @param id
     */
    public void blockDanmu(String id){
        Danmu danmuModel = this.findById(id);
        if( null != danmuModel){
            danmuModel.setBlocked(true);
            danmuRepository.save(danmuModel);
        }
    }


    /**
     * 解除屏蔽弹幕
     * @param id
     */
    public void unblockDanmu(String id){
        Danmu danmuModel = this.findById(id);
        if( null != danmuModel){
            danmuModel.setBlocked(false);
            danmuRepository.save(danmuModel);
        }
    }

    public List<Danmu> findByIds(List<String> danmuIdList){
        return danmuRepository.findByIdIn(danmuIdList);
    }

}
