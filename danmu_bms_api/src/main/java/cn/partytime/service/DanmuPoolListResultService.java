package cn.partytime.service;

import cn.partytime.common.util.ListUtils;
import cn.partytime.model.DanmuPoolListResult;
import cn.partytime.model.danmu.DanmuPool;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.PartyAddressRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by liuwei on 2016/9/6.
 */
@Service
public class DanmuPoolListResultService {

    @Autowired
    private DanmuPoolService danmuPoolService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    @Autowired
    private PartyAddressRelationService partyAddressRelationService;


    private List<DanmuPoolListResult> findByDanmuPools(List<DanmuPool> danmuPoolList) {
        if (null != danmuPoolList && danmuPoolList.size() > 0) {
            List<DanmuPoolListResult> danmuPoolListResults = new ArrayList<DanmuPoolListResult>();
            Set<DanmuPool> allDanmuPoolSet = new HashSet<DanmuPool>();
            allDanmuPoolSet.addAll(danmuPoolList);
            for (DanmuPool danmuPool : danmuPoolList) {
                //当出现有弹幕池合并的情况时
                if (null != danmuPool.getDanmuPoolIdList() && danmuPool.getDanmuPoolIdList().size() > 0) {
                    List<DanmuPool> danmuPoolList1 = danmuPoolService.findAllByIds(danmuPool.getDanmuPoolIdList());
                    allDanmuPoolSet.addAll(danmuPoolList1);
                }
            }
            //将所有的弹幕池相关的信息查询拼装完成
            //此处可以优化，改成按照ids查询,减少查询次数
            Map<String, DanmuPoolListResult> danmuPoolListResultMap = new HashMap<String, DanmuPoolListResult>();
            for (DanmuPool danmuPool : allDanmuPoolSet) {
                if (!StringUtils.isEmpty(danmuPool.getPartyAddressRelationId())) {
                    PartyAddressRelation partyAddressRelation = partyAddressRelationService.findById(danmuPool.getPartyAddressRelationId());
                    Party party = partyService.findById(partyAddressRelation.getPartyId());
                    DanmuAddress address = danmuAddressService.findById(partyAddressRelation.getAddressId());
                    DanmuPoolListResult danmuPoolListResult = new DanmuPoolListResult();
                    danmuPoolListResult.setId(danmuPool.getId());
                    danmuPoolListResult.init(party.getName(), address.getName(), danmuPool.getId());
                    danmuPoolListResultMap.put(danmuPool.getId(), danmuPoolListResult);
                }
            }
            for (DanmuPool danmuPool : danmuPoolList) {
                //当出现有弹幕池合并的情况时
                if (null != danmuPool.getDanmuPoolIdList() && danmuPool.getDanmuPoolIdList().size() > 0) {
                    List<String> partyNameList = new ArrayList<String>();
                    List<String> addressNameList = new ArrayList<String>();
                    List<String> danmuPoolIdList = new ArrayList<String>();
                    for (String danmuPoolId : danmuPool.getDanmuPoolIdList()) {
                        DanmuPoolListResult danmuPoolListResult = danmuPoolListResultMap.get(danmuPoolId);
                        if (null != danmuPoolListResult) {
                            partyNameList.addAll(danmuPoolListResult.getPartyNameList());
                            addressNameList.addAll(danmuPoolListResult.getAddressNameList());
                            danmuPoolIdList.addAll(danmuPoolListResult.getDanmuPoolIdList());
                        }
                    }
                    DanmuPoolListResult dpr = new DanmuPoolListResult();
                    dpr.setId(danmuPool.getId());
                    dpr.setAddressNameList(addressNameList);
                    dpr.setPartyNameList(partyNameList);
                    dpr.setDanmuPoolIdList(danmuPoolIdList);
                    danmuPoolListResults.add(dpr);
                } else {
                    danmuPoolListResults.add(danmuPoolListResultMap.get(danmuPool.getId()));
                }
            }


            return danmuPoolListResults;
        } else {
            return null;
        }
    }

    /**
     * 根据活动id查询弹幕池列表
     *
     * @param partyId
     * @return
     */
    public List<DanmuPoolListResult> findByPartyId(String partyId) {
        List<DanmuPool> danmuPoolList = danmuPoolService.findByPartyId(partyId);
        return findByDanmuPools(danmuPoolList);
    }

    /**
     * 根据活动名称模糊查询
     *
     * @param partyName
     * @return
     */
    public List<DanmuPoolListResult> findByPartyNameLike(String partyName) {
        List<DanmuPool> danmuPoolList = danmuPoolService.findByPartyNameLike(partyName);

        return findByDanmuPools(danmuPoolList);
    }

    /**
     * 合并弹幕池
     *
     * @param danmuPoolId
     * @param mergeDanmuPoolId
     */
    public void mergeDanmuPool(String danmuPoolId, String mergeDanmuPoolId) {

        DanmuPool danmuPool = danmuPoolService.findById(danmuPoolId);
        DanmuPool mergeDanmuPool = danmuPoolService.findById(mergeDanmuPoolId);

        if (null == danmuPool || null == mergeDanmuPool) {
            throw new IllegalArgumentException("合并的弹幕池不存在");
        }

        PartyAddressRelation partyAddressRelation = partyAddressRelationService.findById(danmuPool.getPartyAddressRelationId());
        PartyAddressRelation mergePartyAddressRelation = partyAddressRelationService.findById(mergeDanmuPool.getPartyAddressRelationId());

        if (null == partyAddressRelation || null == mergePartyAddressRelation) {
            throw new IllegalArgumentException("合并的弹幕池没有与活动或者场地关联");
        }

        Party party = partyService.findById(partyAddressRelation.getPartyId());
        Party mergeParty = partyService.findById(mergePartyAddressRelation.getPartyId());
        if (null == party || null == mergeParty) {
            throw new IllegalArgumentException("合并的弹幕池没有与活动关联");
        }

        List<String> danmuPoolList = danmuPool.getDanmuPoolIdList();
        List<String> mergeDanmuPoolList = mergeDanmuPool.getDanmuPoolIdList();
        List<String> poolList = ListUtils.listMerge(danmuPoolList, mergeDanmuPoolList);

        List<DanmuPool> allDanmuPoolList = danmuPoolService.findAllByIds(poolList);
        if (allDanmuPoolList != null && !allDanmuPoolList.isEmpty()) {
            allDanmuPoolList.stream().forEach(dp -> dp.setDanmuPoolIdList(poolList));
        }

        danmuPoolService.updateBatchDanmuPool(allDanmuPoolList);
        //danmuPool.setDanmuPoolIdList(poolList);
        //mergeDanmuPool.setDanmuPoolIdList(poolList);
        //danmuPoolService.updateDanmuPool(danmuPool);
        //danmuPoolService.updateDanmuPool(mergeDanmuPool);

        /*List<String> ids = new ArrayList<String>();
        ids.add(danmuPoolId);
        ids.add(mergeDanmuPoolId);


        DanmuPool dp1 = new DanmuPool();
        dp1.setId(danmuPoolId);
        dp1.setDanmuPoolIdList(ids);
        danmuPoolService.mergeDanmuPool(dp1);
        //当合并同一个活动下的弹幕池时
        if (party.getId().equals(mergeParty.getId())) {
            //逻辑删除弹幕池
            DanmuPool dp2 =danmuPoolService.findById(mergeDanmuPoolId);
            dp2.setIsDelete(1);
            danmuPoolService.updateById(dp2);
        }else{
            //当合并不同活动下的弹幕池时
            DanmuPool dp2 = new DanmuPool();
            dp2.setId(mergeDanmuPoolId);
            dp2.setDanmuPoolIdList(ids);
            danmuPoolService.mergeDanmuPool(dp2);
        }*/
    }

    /**
     * 取消合并
     *
     * @param danmuPoolId
     */
    public void cancelMergeDanmuPool(String danmuPoolId) {
        DanmuPool danmuPool = danmuPoolService.findById(danmuPoolId);
        PartyAddressRelation partyAddressRelation = partyAddressRelationService.findById(danmuPool.getPartyAddressRelationId());
        if (null == danmuPool || null == partyAddressRelation) {
            throw new IllegalArgumentException("取消合并的弹幕池不存在或者未关联活动与场地");
        }

        List<DanmuPool> danmuPoolList = danmuPoolService.findAllByIds(danmuPool.getDanmuPoolIdList());
        if(danmuPoolList!=null && !danmuPoolList.isEmpty()){
            //danmuPoolList.stream().filter((name) -> (danmuPoolList.test(name)));
            for(DanmuPool dp:danmuPoolList){
                /*List<String> poolList = dp.getDanmuPoolIdList();
                if(dp.getDanmuPoolIdList().equals(danmuPoolId)){
                    poolList = new ArrayList<String>();
                }*/
                List<String> list = new ArrayList<String>();
                list.add(dp.getId());
                dp.setDanmuPoolIdList(list);
            }
            danmuPoolService.updateBatchDanmuPool(danmuPoolList);
        }

        /*if (null != danmuPoolList && danmuPoolList.size() > 0) {
            for (DanmuPool dp : danmuPoolList) {
                dp.setDanmuPoolIdList(null);
                if (dp.getIsDelete() == 1) {
                    dp.setIsDelete(0);
                }
                danmuPoolService.updateById(dp);
            }
        }
        danmuPool.setDanmuPoolIdList(null);
        danmuPoolService.updateById(danmuPool);*/


    }
}
