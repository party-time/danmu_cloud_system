package cn.partytime.service;


import cn.partytime.model.PageResultModel;
import cn.partytime.model.danmu.DanmuLibrary;
import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.model.danmu.PreDanmu;
import cn.partytime.repository.danmu.DanmuLibraryPartyRepository;
import cn.partytime.repository.danmu.DanmuLibraryRepository;
import cn.partytime.repository.manager.PreDanmuRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwei on 16/6/15.
 */

@Service
@Slf4j
public class PreDanmuService {


    @Resource(name = "managerMongoTemplate")
    private MongoTemplate managerMongoTemplate;

    @Autowired
    private PreDanmuRepository preDanmuRepository;

    @Autowired
    private DanmuLibraryRepository danmuLibraryRepository;

    @Autowired
    private DanmuLibraryPartyRepository danmuLibraryPartyRepository;


    /**
     * 删除预制弹幕
     * @param id
     */
    public void deletePreDanmu(String id){
        preDanmuRepository.delete(id);
    }

    /**
     * 保存一个预制弹幕
     * @param preDanmu
     * @return
     */
    public PreDanmu save(PreDanmu preDanmu){
        DanmuLibrary danmuLibrary = danmuLibraryRepository.findOne(preDanmu.getDanmuLibraryId());
        if( null == danmuLibrary){
            throw new IllegalArgumentException("关联的弹幕库不存在");
        }
        return preDanmuRepository.insert(preDanmu);
    }

    public PreDanmu saveNew(PreDanmu preDanmu){
        DanmuLibrary danmuLibrary = danmuLibraryRepository.findOne(preDanmu.getDanmuLibraryId());
        if(danmuLibrary!=null){
            return preDanmuRepository.save(preDanmu);
        }
        return null;

    }

    /**
     * 保存一组预制弹幕
     * @param preDanmuList
     * @return
     */
    public List<PreDanmu> save(String danmuLibraryId , List<PreDanmu> preDanmuList){
        DanmuLibrary danmuLibrary = danmuLibraryRepository.findOne(danmuLibraryId);
        if( null == danmuLibrary){
            throw new IllegalArgumentException("关联的弹幕库不存在");
        }
        if( null != preDanmuList){
            for(PreDanmu preDanmu : preDanmuList){
                preDanmu.setDanmuLibraryId(danmuLibraryId);
            }
        }
        return preDanmuRepository.insert(preDanmuList);
    }

    /**
     * 删除一个预制弹幕
     * @param id
     */
    public void deleteById(String id){
        preDanmuRepository.delete(id);
    }

    /**
     * 查询所有预制弹幕 根据弹幕播放时间排序
     * @return
     */
    public List<PreDanmu> findAll(){
        Sort sort = new Sort(Sort.Direction.DESC,"startTime");
        return preDanmuRepository.findAll(sort);
    }

    /**
     * 查找活动下的预制弹幕
     * @param
     * @return
     */
    /*public List<PreDanmu> findByPartyId(String partyId){
        DanmuLibraryParty danmuLibraryParty = danmuLibraryPartyRepository.findByPartyId(partyId);
        if( null == danmuLibraryParty){
            throw new IllegalArgumentException("该活动没有关联弹幕库");
        }
        Sort sort = new Sort(Sort.Direction.ASC,"createTime");
        return preDanmuRepository.findByDanmuLibraryId(danmuLibraryParty.getDanmuLibraryId(),sort);
    }*/




    public long countByDanmuLibraryId(String dlId){
        return preDanmuRepository.countByDanmuLibraryId(dlId);
    }

    public Page<PreDanmu> findPageByDLId(int page, int size, String dlId){
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        PageRequest pageRequest = new PageRequest(page,size,sort);
        return preDanmuRepository.findByDanmuLibraryId(dlId,pageRequest);
    }

    public PageResultModel<PreDanmu> findByDanmuLibraryIdAndMsgLike(int page, int size, String dlId, String msg){
        //Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        //PageRequest pageRequest = new PageRequest(page,size,sort);
        //return preDanmuRepository.findByDanmuLibraryIdAndMsgLike(dlId,msg,pageRequest);
        /*CommandResult commandResult=managerMongoTemplate.executeCommand(jsonSql);
        System.out.println();
        BasicDBList list = (BasicDBList)commandResult.get("values");
        for (int i = 0; i < list.size(); i ++) {
            System.out.println(list.get(i));
        }
        System.out.println();
        return null;*/
        Criteria criteria = new Criteria();
        //criteria.where("content.message").regex("/^111*/");
        criteria.andOperator(Criteria.where("content.message").regex(".*?" + msg + ".*"),Criteria.where("danmuLibraryId").is(dlId));
        Map<String, Object> result = new HashMap<>();
        Query query = new Query(criteria);
        query.skip(page * size);
        query.limit(size);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        List<PreDanmu> list = this.managerMongoTemplate.find(query, PreDanmu.class);
        long count = this.managerMongoTemplate.count(query, PreDanmu.class);

        //pageResultModel<PreDanmu>
        //result.put("datas", list);
        //result.put("size", count);

        PageResultModel<PreDanmu> preDanmuModelPageResultModel = new PageResultModel<>();
        preDanmuModelPageResultModel.setTotal(count);
        preDanmuModelPageResultModel.setRows(list);
        return preDanmuModelPageResultModel;
    }


    /**
     * 通过活动编号查询预制弹幕数
     * @param danmuLibraryId
     * @return
     */
    public long findPreDanmuCountByDanmuLibrary(String danmuLibraryId){
        return preDanmuRepository.findByDanmuLibraryId(danmuLibraryId).size();

    }




    public List<DanmuLibrary> findAllDanmuLibrary(){
        return danmuLibraryRepository.findAll();
    }



    public DanmuLibrary save(String name){
        DanmuLibrary danmuLibraryList = danmuLibraryRepository.findByName(name);
        if( null != danmuLibraryList){
            throw new IllegalArgumentException("弹幕库名称有重复");
        }
        DanmuLibrary danmuLibrary = new DanmuLibrary();
        danmuLibrary.setName(name);
        return danmuLibraryRepository.save(danmuLibrary);
    }

    public void delDanmuLibrary(String id){
        danmuLibraryRepository.delete(id);
    }
    public List<DanmuLibrary> findByIdIn(List<String> ids){
        return danmuLibraryRepository.findByIdIn(ids);
    }

    public List<DanmuLibrary> findByIdNotIn(List<String> ids){
        return danmuLibraryRepository.findByIdNotIn(ids);
    }
}
