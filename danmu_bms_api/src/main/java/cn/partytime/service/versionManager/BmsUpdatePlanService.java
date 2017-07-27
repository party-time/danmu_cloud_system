package cn.partytime.service.versionManager;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.manager.UpdatePlan;
import cn.partytime.model.manager.Version;
import cn.partytime.model.versionManager.JavaUpdatePlanResult;
import cn.partytime.model.versionManager.UpdatePlanResult;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2017/2/14.
 */
@Service
public class BmsUpdatePlanService {

    private static final Logger logger = LoggerFactory.getLogger(BmsUpdatePlanService.class);

    @Autowired
    private UpdatePlanService updatePlanService;

    @Autowired
    private VersionService versionService;

    public PageResultModel<UpdatePlanResult> findByAddressId(String addressId,Integer pageSize,Integer pageNumber){
        PageResultModel<UpdatePlanResult> pageResultModel = new PageResultModel<>();
        Page<UpdatePlan> updatePlanPage = updatePlanService.findByAddressId(addressId,pageNumber,pageSize);
        if( null != updatePlanPage){
            List<String> versionIdList = new ArrayList<>();
            for(UpdatePlan updatePlan : updatePlanPage.getContent()){
                versionIdList.add(updatePlan.getVersionId());
            }
            List<Version> versionList = versionService.findByIds(versionIdList);
            List<UpdatePlanResult> updatePlanResultList = new ArrayList<>();
            for(UpdatePlan updatePlan : updatePlanPage.getContent()){
                UpdatePlanResult updatePlanResult = new UpdatePlanResult();
                updatePlanResult.setUpdatePlan(updatePlan);
                for(Version version : versionList){
                    if( version.getId().equals(updatePlan.getVersionId())){
                        updatePlanResult.setVersion(version);
                    }
                }

                updatePlanResultList.add(updatePlanResult);
            }

            pageResultModel.setTotal(updatePlanPage.getTotalElements());
            pageResultModel.setRows(updatePlanResultList);
        }

        return pageResultModel;
    }

    public PageResultModel<Version> findByAddressIdNotIn(String addressId, Integer pageSize, Integer pageNumber){
        List<UpdatePlan> updatePlanList = updatePlanService.findByAddressId(addressId);
        PageResultModel<Version> versionPageResultModel = new PageResultModel<>();
        if( null != updatePlanList || updatePlanList.size() > 0){
            List<String> versionIdList = new ArrayList<>();
            for(UpdatePlan updatePlan : updatePlanList){
                versionIdList.add(updatePlan.getVersionId());
            }

            Page<Version> versionPage = versionService.findByIdNotIn(versionIdList,pageNumber,pageSize);
            versionPageResultModel.setRows(versionPage.getContent());
            versionPageResultModel.setTotal(versionPage.getTotalElements());
            return versionPageResultModel;
        }else{
            Page<Version> versionPage = versionService.findAll(pageNumber,pageSize);
            versionPageResultModel.setRows(versionPage.getContent());
            versionPageResultModel.setTotal(versionPage.getTotalElements());
            return versionPageResultModel;
        }
    }

    public List<JavaUpdatePlanResult> findByAddressId(String addressId){
        List<UpdatePlan> updatePlanList = updatePlanService.findByAddressIdAndStatus(addressId,0);
        List<JavaUpdatePlanResult> javaUpdatePlanResults = new ArrayList<>();
        if( null != updatePlanList){
            for(UpdatePlan updatePlan : updatePlanList){
                logger.info("updatePlan:{}", JSON.toJSONString(updatePlan));
                JavaUpdatePlanResult javaUpdatePlanResult = new JavaUpdatePlanResult();
                javaUpdatePlanResult.setId(updatePlan.getId());
                Version version = versionService.findById(updatePlan.getVersionId());
                javaUpdatePlanResult.setType(version.getType());
                javaUpdatePlanResult.setVersion(version.getVersion());
                javaUpdatePlanResult.setUpdateDate(updatePlan.getUpdatePlanTime());
                javaUpdatePlanResults.add(javaUpdatePlanResult);
            }
        }

        return javaUpdatePlanResults;
    }
}
