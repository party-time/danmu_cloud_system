package cn.partytime.service.danmu;

import cn.partytime.model.danmu.DanmuPool;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.repository.danmu.DanmuPoolRepository;
import cn.partytime.service.party.PartyAddressRelationService;
import cn.partytime.service.party.PartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */

@Service
@Slf4j
public class DanmuPoolService {

    @Resource(name = "danmuMongoTemplate")
    private MongoTemplate danmuMongoTemplate;

    @Autowired
    private DanmuPoolRepository danmuPoolRepository;

    @Autowired
    private PartyAddressRelationService partyAddressRelationService;

    @Autowired
    private PartyService partyService;

    public void updateDanmuPool(DanmuPool danmuPool) {
        danmuPoolRepository.save(danmuPool);
    }

    public void updateBatchDanmuPool(List<DanmuPool> danmuPoolList){
        danmuPoolRepository.save(danmuPoolList);
    }

    public DanmuPool save(String partyAddressRelationId) {
        DanmuPool danmuPool = new DanmuPool();
        danmuPool.setPartyAddressRelationId(partyAddressRelationId);
        danmuPool = danmuPoolRepository.insert(danmuPool);
        List<String> list = new ArrayList<String>();
        list.add(danmuPool.getId());
        danmuPool.setDanmuPoolIdList(list);
        return danmuPoolRepository.save(danmuPool);
    }

    public DanmuPool mergeDanmuPool(DanmuPool danmuPool) {
        DanmuPool dp = danmuPoolRepository.findByIdAndIsDelete(danmuPool.getId(), 0);
        if (null != dp) {
            List oldList = dp.getDanmuPoolIdList();
            if (null == oldList) {
                oldList = new ArrayList<String>();
            }
            oldList.addAll(danmuPool.getDanmuPoolIdList());
            dp.setDanmuPoolIdList(oldList);
            return danmuPoolRepository.save(dp);
        } else {
            throw new IllegalArgumentException("danmu pool " + danmuPool.getId() + "is null");
        }

    }



    public DanmuPool saveDanmuPool(String partyId, String addressId){
        partyAddressRelationService.save(partyId,addressId);
        return save(partyId,addressId);
    }

    /**
     * 根据活动及场地编号创建弹幕池
     *
     * @param partyId
     * @param addressId
     * @return
     */
    public DanmuPool save(String partyId, String addressId) {
        PartyAddressRelation partyAddressRelation = partyAddressRelationService.findByPartyIdAndAddressId(partyId, addressId);
        if (null != partyAddressRelation) {
            return this.save(partyAddressRelation.getId());
        } else {
            throw new IllegalArgumentException("save danmu pool ,partyAddressRelation is null ");
        }

    }

    public void deleteById(String id) {
        danmuPoolRepository.delete(id);
    }

    public DanmuPool updateById(DanmuPool danmuPool) {
        return danmuPoolRepository.save(danmuPool);
    }

    public DanmuPool findById(String id) {
        return danmuPoolRepository.findByIdAndIsDelete(id, 0);
    }

    //查询包括被逻辑删除的数据
    public DanmuPool findAllById(String id) {
        return danmuPoolRepository.findById(id);
    }

    /**
     * 获取地址与活动关系数据
     *
     * @param partyAddressRelationId
     * @return
     */
    public DanmuPool findByPartyAddressRelationId(String partyAddressRelationId) {
        Criteria criteria = Criteria.where("partyAddressRelationId").is(partyAddressRelationId);
        Query query = new Query(criteria);
        DanmuPool danmuPool = danmuMongoTemplate.findOne(query, DanmuPool.class);
        return danmuPool;
    }

    /**
     * 通过活动编号和地址编号获取弹幕池
     *
     * @param addressId
     * @param partyId
     * @return
     */
    public DanmuPool findDanmuPoolbyPartyIdAndAddressId(String addressId, String partyId) {
        PartyAddressRelation partyAddressRelation = partyAddressRelationService.findByPartyIdAndAddressId(partyId, addressId);
        if( null != partyAddressRelation){
            return findByPartyAddressRelationId(partyAddressRelation.getId());
        }else{
            return null;
        }
    }

    /**
     * 查找某个活动的所有弹幕池
     *
     * @param partyId
     * @return
     */
    public List<DanmuPool> findByPartyId(String partyId) {
        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByPartyId(partyId);
        List<String> partyAddressRelationIds = new ArrayList<String>();
        if (null != partyAddressRelationList && partyAddressRelationList.size() > 0) {
            for (PartyAddressRelation partyAddressRelation : partyAddressRelationList) {
                partyAddressRelationIds.add(partyAddressRelation.getId());
            }
        }
        return danmuPoolRepository.findByPartyAddressRelationIdInAndIsDelete(partyAddressRelationIds, 0);
    }

    public List<DanmuPool> findByPartyIdAndAddressList(String partyId, List<String> addressList) {
        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByPartyIdAndAddressList(partyId,addressList);
        List<String> partyAddressRelationIds = new ArrayList<String>();
        if (null != partyAddressRelationList && partyAddressRelationList.size() > 0) {
            for (PartyAddressRelation partyAddressRelation : partyAddressRelationList) {
                partyAddressRelationIds.add(partyAddressRelation.getId());
            }
        }
        return danmuPoolRepository.findByPartyAddressRelationIdInAndIsDelete(partyAddressRelationIds, 0);
    }



    public List<DanmuPool> findAllByIds(List<String> ids) {
        if (null == ids || ids.size() == 0) {
            return null;
        }
        return danmuPoolRepository.findByIdIn(ids);
    }

    public List<DanmuPool> findByIds(List<String> ids) {
        if (null == ids || ids.size() == 0) {
            return null;
        }
        return danmuPoolRepository.findByIdInAndIsDelete(ids, 0);
    }


    /**
     * 查找某个活动的所有弹幕池
     *
     * @param
     * @return
     */
    public List<DanmuPool> findByPartyNameLike(String partyName) {
        List<Party> partyList = partyService.findByNameLike(partyName);
        if (null != partyList && partyList.size() > 0) {
            List<String> partyIdList = new ArrayList<String>();
            for (Party party : partyList) {
                partyIdList.add(party.getId());
            }
            List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByPartyIds(partyIdList);
            List<String> partyAddressRelationIds = new ArrayList<String>();
            if (null != partyAddressRelationList && partyAddressRelationList.size() > 0) {
                for (PartyAddressRelation partyAddressRelation : partyAddressRelationList) {
                    partyAddressRelationIds.add(partyAddressRelation.getId());
                }
            }
            return danmuPoolRepository.findByPartyAddressRelationIdInAndIsDelete(partyAddressRelationIds, 0);
        } else {
            return null;
        }

    }


}
