package cn.partytime.service.danmu;

import cn.partytime.logic.danmu.PageResultModel;
import cn.partytime.model.danmu.DanmuModel;
import cn.partytime.model.danmu.PreDanmuModel;
import cn.partytime.repository.danmu.DanmuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwei on 16/6/15.
 */

@RestController
@RequestMapping("/danmu")
public class DanmuService {

    @Resource(name = "danmuMongoTemplate")
    private MongoTemplate danmuMongoTemplate;

    @Autowired
    private DanmuRepository danmuRepository;


    @RequestMapping(value = "/save" ,method = RequestMethod.POST)
    public DanmuModel save(@RequestBody  DanmuModel danmuModel) {
        return danmuRepository.save(danmuModel);
    }



    public void deleteById(String id) {
        danmuRepository.delete(id);
    }

    @RequestMapping(value = "/findById" ,method = RequestMethod.GET)
    public DanmuModel findById(@RequestParam String id) {
        return danmuRepository.findById(id);
    }

    public List<DanmuModel> findByDanmuPoolId(String danmuPoolId) {
        return danmuRepository.findByDanmuPoolId(danmuPoolId);
    }

    public Page<DanmuModel> findDanmuListByPage(int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuRepository.findAll(pageRequest);
    }



    @RequestMapping(value = "/findDanmuByIsBlocked" ,method = RequestMethod.GET)
    public List<DanmuModel> findDanmuByIsBlocked(@RequestParam int page, @RequestParam int size, @RequestParam boolean isBlocked){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        Page<DanmuModel> danmuModelPage = danmuRepository.findByIsBlocked(isBlocked,pageRequest);
        return danmuModelPage.getContent();
    }


    public PageResultModel<DanmuModel> findByMsgLike(String msg, int page, int size){
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
        List<DanmuModel> list = this.danmuMongoTemplate.find(query, DanmuModel.class);
        long count = this.danmuMongoTemplate.count(query, PreDanmuModel.class);

        //pageResultModel<PreDanmuModel>
        //result.put("datas", list);
        //result.put("size", count);

        PageResultModel<DanmuModel> preDanmuModelPageResultModel = new PageResultModel<>();
        preDanmuModelPageResultModel.setTotal(count);
        preDanmuModelPageResultModel.setRows(list);
        return preDanmuModelPageResultModel;
    }




    public List<DanmuModel> findDanmuListByPartyIdAndTimeAndDanmuPool(String partyId, int time, List<String>danmuPoolIdList, int limit){
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
        return danmuMongoTemplate.find(query,DanmuModel.class);
    }




    public Page<DanmuModel> findByDanmuPoolIdAndDanmuSrc(int page, int size, String danmuPoolId, boolean isBlocked, int danmuSrc) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuRepository.findByDanmuPoolIdAndDanmuSrcAndIsBlocked(danmuPoolId,danmuSrc,isBlocked,pageRequest);
    }


    public Page<DanmuModel> findByDanmuSrcAndIsBlockedAndViewFlgAndDanmuPoolIdWithin(int danmuSrc, boolean isBlocked, boolean viewFlg, Integer page, Integer size, List<String> danmuPoolIdList) {
        Sort sort = new Sort(Sort.Direction.ASC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuRepository.findByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlg(danmuPoolIdList,danmuSrc,isBlocked,viewFlg,pageRequest);
    }


    public Page<DanmuModel> findByDanmuPoolIdAndDanmuSrcAndIsBlockedAndViewFlg(String danmuPoolId, int danmuSrc, boolean isBlocked, boolean viewFlg, Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuRepository.findByDanmuPoolIdAndDanmuSrcAndIsBlockedAndViewFlg(danmuPoolId,danmuSrc,isBlocked,viewFlg,pageRequest);
    }


    public long countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlg(List<String> danmuPoolIdList,int danmuSrc,boolean isBlocked,boolean viewFlg){
        return danmuRepository.countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlg(danmuPoolIdList,danmuSrc,isBlocked,viewFlg);
    }

    /**
     * 屏蔽弹幕
     * @param id
     */
    public void blockDanmu(String id){
        DanmuModel danmuModel = this.findById(id);
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
        DanmuModel danmuModel = this.findById(id);
        if( null != danmuModel){
            danmuModel.setBlocked(false);
            danmuRepository.save(danmuModel);
        }
    }

}
