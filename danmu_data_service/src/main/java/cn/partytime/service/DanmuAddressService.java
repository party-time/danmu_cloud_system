package cn.partytime.service;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.repository.manager.DanmuAddressRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwei on 16/6/15.
 */

@Service
@Slf4j
public class DanmuAddressService {

    @Autowired
    private DanmuAddressRepository danmuAddressRepository;

    @Resource(name = "managerMongoTemplate")
    private MongoTemplate managerMongoTemplate;

    @Autowired
    private PartyAddressRelationService partyAddressRelationService;

    @Autowired
    private PartyService partyService;

    public DanmuAddress save(DanmuAddress danmuAddress) {
        return danmuAddressRepository.insert(danmuAddress);
    }

    public DanmuAddress update(DanmuAddress danmuAddress) {
        return danmuAddressRepository.save(danmuAddress);
    }

    public void deleteById(String id) {
        danmuAddressRepository.delete(id);
        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByAddressId(id);
        if (null != partyAddressRelationList && partyAddressRelationList.size() > 0) {
            for (PartyAddressRelation partyAddressRelation : partyAddressRelationList) {
                partyAddressRelationService.del(partyAddressRelation.getId());
            }
        }
    }

    public DanmuAddress updateById(DanmuAddress danmuAddress) {
        return danmuAddressRepository.save(danmuAddress);
    }

    public DanmuAddress findById(String id) {
        return danmuAddressRepository.findById(id);
    }

    public PageResultModel<DanmuAddress> findAll(int page, int size) {
        Sort timeSort = new Sort(Sort.Direction.DESC, "createTime");
        Sort typeSort = new Sort(Sort.Direction.ASC,"type");
        typeSort.and(timeSort);
        PageRequest pageRequest = new PageRequest(page, size, typeSort);
        Page<DanmuAddress> danmuAddressPage = danmuAddressRepository.findAll(pageRequest);
        PageResultModel<DanmuAddress>  pageResultModel = new PageResultModel<>();
        pageResultModel.setTotal(danmuAddressPage.getTotalElements());
        pageResultModel.setRows(danmuAddressPage.getContent());
        return pageResultModel;
    }

    public Page<DanmuAddress> findPageByType(Integer type,int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuAddressRepository.findByType(type,pageRequest);
    }

    public List<DanmuAddress> findByType(Integer type) {
        return danmuAddressRepository.findByType(type);
    }



    /**
     * 分页查询活动未关联的场地
     *
     * @param partyId
     * @param page
     * @param size
     * @return
     */
    public Page<DanmuAddress> findAddressNotEqualsPartyId(String partyId, int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        //查询此活动已经关联的场地
        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByPartyId(partyId);
        List<String> addressIds = new ArrayList<String>();
        if (null != partyAddressRelationList && partyAddressRelationList.size() > 0) {
            for (PartyAddressRelation partyAddressRelation : partyAddressRelationList) {
                addressIds.add(partyAddressRelation.getAddressId());
            }
        }
        return danmuAddressRepository.findByIdNotIn(addressIds, pageRequest);
    }


    public PageResultModel<DanmuAddress> findAddressNotIn(List<String> addressIdList , int page, int size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        Page<DanmuAddress> danmuAddressPage = danmuAddressRepository.findByIdNotIn(addressIdList, pageRequest);
        PageResultModel<DanmuAddress> pageResultModel = new PageResultModel<>();
        pageResultModel.setRows(danmuAddressPage.getContent());
        pageResultModel.setTotal(danmuAddressPage.getTotalElements());
        return pageResultModel;
    }

    /**
     * 查询活动下的所有场地
     *
     * @param partyId
     * @return
     */
    public List<DanmuAddress> findAddressByPartyId(String partyId) {
        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByPartyId(partyId);
        List<String> addressIds = new ArrayList<String>();
        if (null != partyAddressRelationList && partyAddressRelationList.size() > 0) {
            for (PartyAddressRelation partyAddressRelation : partyAddressRelationList) {
                addressIds.add(partyAddressRelation.getAddressId());
            }
        }
        return danmuAddressRepository.findByIdIn(addressIds);
    }

    public List<String> findAddress(String partyId) {
        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByPartyId(partyId);
        List<String> addressIds = new ArrayList<String>();
        if (null != partyAddressRelationList && partyAddressRelationList.size() > 0) {
            for (PartyAddressRelation partyAddressRelation : partyAddressRelationList) {
                addressIds.add(partyAddressRelation.getAddressId());
            }
        }
        return addressIds;
    }

    public Page<DanmuAddress>  findAddressByPartyId(List<String> addressIds,int page, int size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuAddressRepository.findByIdIn(addressIds,pageRequest);
    }


    /**
     * 通过经纬度获取地址列表
     *
     * @param longitude
     * @param latitude
     * @param maxDistance 最大距离
     * @return 非NULL的list
     */
    public List<String> findAddressIdList(Double longitude, Double latitude, long maxDistance) {

        BasicDBObject basicDBObject4 = new BasicDBObject("coordinates", new double[]{longitude, latitude});

        BasicDBObject geometryObject = new BasicDBObject("$geometry", basicDBObject4);
        geometryObject.append("$maxDistance", maxDistance);

        BasicDBObject nearObject = new BasicDBObject("$near", geometryObject);
        BasicDBObject basicDBObject1 = new BasicDBObject("location", nearObject);

        DBCursor cur1 = managerMongoTemplate.getCollection("danmu_address").find(basicDBObject1);
        List<String> danmuAddressIdList = new ArrayList<String>();
        while (cur1.hasNext()) {
            //DanmuAddress o = (DanmuAddress) cur1.next();
            BasicDBObject basicDBObject = (BasicDBObject) cur1.next();
            danmuAddressIdList.add(String.valueOf(basicDBObject.get("_id")));
        }
        return danmuAddressIdList;
    }


    /**
     * 通过地址id list 获取所有地址信息
     * @param addressIdlist
     * @return
     */
    public List<DanmuAddress> findDanmuAddressByIdList(List<String> addressIdlist) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("id").in(addressIdlist)
        );
        Query query = new Query();
        query.addCriteria(criteria);
        return managerMongoTemplate.find(query, DanmuAddress.class);
    }


    public PageResultModel<DanmuAddress> findDanmuAddressByPartyId(String partyId,Integer pageNum ,Integer pageSize){
        Party party = partyService.findById(partyId);

        Page<DanmuAddress> page = null;
        PageResultModel<DanmuAddress> danmuAddressPageResultModel = new PageResultModel<>();
        if( null == party){
            return null;
        }
        if(party.getType() == 0){
            List<String> danmuAddressIdList = this.findAddress(partyId);
            page = this.findAddressByPartyId(danmuAddressIdList,pageNum,pageSize);
        }else if(party.getType() == 1){
            page =  this.findPageByType(0,pageNum,pageSize);
        }
        if(page!=null){
            danmuAddressPageResultModel.setTotal(page.getTotalElements());
            danmuAddressPageResultModel.setRows(page.getContent());
        }
        return danmuAddressPageResultModel;

    }

    /**
     * 更新影院店铺状态
     * @param id
     * @param shopStatus
     */
    public void updateShopStatus(String id,Integer shopStatus){
        DanmuAddress danmuAddress = danmuAddressRepository.findById(id);
        if( null!= danmuAddress){
            danmuAddress.setShopStatus(shopStatus);
            danmuAddressRepository.save(danmuAddress);
        }
    }

    public void updateControlerStatus(String id,String key,Integer status){
        DanmuAddress danmuAddress = danmuAddressRepository.findById(id);
        if( null != danmuAddress ){
            Map<String,Integer> map = danmuAddress.getControlerStatus();
            if( null == map){
                map = new HashMap<>();
            }
            map.put(key,status);
            danmuAddress.setControlerStatus(map);
            danmuAddressRepository.save(danmuAddress);
        }
    }

    public void updateControlerStatus(String id,Map<String,Integer> map){
        DanmuAddress danmuAddress = danmuAddressRepository.findById(id);
        if( null != danmuAddress ){
            danmuAddress.setControlerStatus(map);
            danmuAddressRepository.save(danmuAddress);
        }
    }

    public void updateAdTime(String addressId,Integer adTime){
        DanmuAddress danmuAddress = danmuAddressRepository.findById(addressId);
        if( null != danmuAddress){
            danmuAddress.setAdTime(adTime);
            danmuAddressRepository.save(danmuAddress);
        }
    }

    public List<DanmuAddress> findAll(){
        return danmuAddressRepository.findAll();
    }

}
