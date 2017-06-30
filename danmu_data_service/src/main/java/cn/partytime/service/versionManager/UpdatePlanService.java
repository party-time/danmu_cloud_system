package cn.partytime.service.versionManager;

import cn.partytime.model.manager.UpdatePlan;
import cn.partytime.model.manager.UpdatePlanMachine;
import cn.partytime.repository.manager.versionManager.UpdatePlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 2017/2/13.
 */
@Service
@Slf4j
public class UpdatePlanService {

    @Autowired
    private UpdatePlanRepository updatePlanRepository;


    public UpdatePlan save(String addressId,String versionId,Date updateTime){
        UpdatePlan updatePlan = new UpdatePlan();
        updatePlan.setAddressId(addressId);
        updatePlan.setVersionId(versionId);
        updatePlan.setUpdatePlanTime(updateTime);
        return updatePlanRepository.insert(updatePlan);
    }

    public void del(String id){
        updatePlanRepository.delete(id);
    }

    public Page<UpdatePlan> findByAddressId(String addressId,int page,int size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return updatePlanRepository.findByAddressId(addressId,pageRequest);
    }

    public List<UpdatePlan> findByAddressId(String addressId){
        return updatePlanRepository.findByAddressId(addressId);
    }

    public List<UpdatePlan> findByAddressIdAndStatus(String addressId,Integer status){
        List<UpdatePlan> updatePlanList = updatePlanRepository.findByAddressId(addressId);
        List<UpdatePlan> updatePlanResultList = new ArrayList<>();
        if(null != updatePlanList){
            for(UpdatePlan updatePlan : updatePlanList){
                if( null != updatePlan ){
                    if( null == updatePlan.getUpdatePlanMachineList()){
                        updatePlanResultList.add(updatePlan);
                    }else{
                        int i = 0;
                        for(UpdatePlanMachine updatePlanMachine : updatePlan.getUpdatePlanMachineList()){
                            if( updatePlanMachine.getStatus() == status){
                                ++i;
                            }
                        }
                        if( i > 0){
                            updatePlanResultList.add(updatePlan);
                        }

                    }

                }
            }
        }

        return updatePlanResultList;
    }

    /**
     * 查找该场地下更新状态不等于
     * @param addressId
     * @param status
     * @return
     */
    public List<UpdatePlan> findByAddressIdAndStatusNot(String addressId,Integer status){
        List<UpdatePlan> updatePlanList = updatePlanRepository.findByAddressId(addressId);
        List<UpdatePlan> updatePlanResultList = new ArrayList<>();
        if(null != updatePlanList){
            for(UpdatePlan updatePlan : updatePlanList){
                if( null != updatePlan ){
                    if( null == updatePlan.getUpdatePlanMachineList()){
                        updatePlanResultList.add(updatePlan);
                    }else{
                        int i = 0;
                        for(UpdatePlanMachine updatePlanMachine : updatePlan.getUpdatePlanMachineList()){
                            if( updatePlanMachine.getStatus() == status){
                                ++i;
                            }
                        }
                        if( i == 0){
                            updatePlanResultList.add(updatePlan);
                        }

                    }

                }
            }
        }
        return updatePlanResultList;
    }

    public void update(String id,Integer status,String machineNum){
        UpdatePlan updatePlan = updatePlanRepository.findOne(id);
        if( null != updatePlan){
            List<UpdatePlanMachine> updatePlanMachineList = updatePlan.getUpdatePlanMachineList();
            if( null == updatePlanMachineList){
                updatePlanMachineList = new ArrayList<>();
                UpdatePlanMachine updatePlanMachine = new UpdatePlanMachine();
                updatePlanMachine.setMachineNum(machineNum);
                updatePlanMachine.setStatus(status);
                updatePlanMachineList.add(updatePlanMachine);
                updatePlan.setUpdatePlanMachineList(updatePlanMachineList);
            }else{
                int i = 0;
                for(UpdatePlanMachine updatePlanMachine : updatePlanMachineList){
                    if(machineNum.equals(updatePlanMachine.getMachineNum())){
                        updatePlanMachine.setStatus(status);
                        ++i;
                    }
                }
                if( i == 0){
                    UpdatePlanMachine updatePlanMachine = new UpdatePlanMachine();
                    updatePlanMachine.setMachineNum(machineNum);
                    updatePlanMachine.setStatus(status);
                    updatePlanMachineList.add(updatePlanMachine);
                }
                updatePlan.setUpdatePlanMachineList(updatePlanMachineList);

            }
            updatePlanRepository.save(updatePlan);
        }

    }


}
