package cn.partytime.service;

import cn.partytime.model.manager.RealTimeDmAddress;
import cn.partytime.repository.manager.RealTimeDmAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2018/1/3.
 */
@Service
@Slf4j
public class RealTimeDmAddressService {

    @Autowired
    private RealTimeDmAddressRepository realTimeDmAddressRepository;

    public RealTimeDmAddress save(RealTimeDmAddress realTimeDmAddress){
        return realTimeDmAddressRepository.insert(realTimeDmAddress);
    }

    public void saveRealTimeDmAddress(String name , String addressIds){
        if(!StringUtils.isEmpty(name)){
            RealTimeDmAddress rtda = new RealTimeDmAddress();
            rtda.setName(name);
            rtda = this.save(rtda);
            if( null != rtda && !StringUtils.isEmpty(addressIds)){
                if(addressIds.indexOf(",")!=-1) {
                    String[] addressIdStrs = addressIds.split(",");
                    for( String addressId : addressIdStrs){
                        RealTimeDmAddress rtda1 = new RealTimeDmAddress();
                        rtda1.setParentId(rtda.getId());
                        rtda1.setAddressId(addressId);
                        this.save(rtda1);
                    }
                }
            }
        }
    }


    public void saveList(List<RealTimeDmAddress> realTimeDmAddressList){
        if( null != realTimeDmAddressList && realTimeDmAddressList.size() > 0){
            RealTimeDmAddress rtda = null;
            for(RealTimeDmAddress realTimeDmAddress : realTimeDmAddressList){
                if(!StringUtils.isEmpty(realTimeDmAddress.getName())){
                    rtda = this.save(realTimeDmAddress);
                }
            }
            for(RealTimeDmAddress realTimeDmAddress : realTimeDmAddressList){
                if(StringUtils.isEmpty(realTimeDmAddress.getName())){
                    realTimeDmAddress.setParentId(rtda.getId());

                    RealTimeDmAddress rtda1 = this.findByAddressId(realTimeDmAddress.getAddressId());
                    if( null == rtda1){
                        this.save(realTimeDmAddress);
                    }
                }
            }
        }
    }

    public RealTimeDmAddress findById(String id){
        return realTimeDmAddressRepository.findOne(id);
    }

    public List<RealTimeDmAddress> findAllById(String id){
        RealTimeDmAddress realTimeDmAddress = this.findById(id);
        if( null != realTimeDmAddress && StringUtils.isEmpty(realTimeDmAddress.getParentId())){
            return this.findByParentId(realTimeDmAddress.getParentId());
        }else{
            return null;
        }
    }

    public RealTimeDmAddress findByAddressId(String addressId){
        return realTimeDmAddressRepository.findByAddressId(addressId);
    }

    public List<RealTimeDmAddress> findByParentId(String parentId){
        return realTimeDmAddressRepository.findByParentId(parentId);
    }

    /**
     * 按照场地id，查询所有的场地id
     * @param addressId
     * @return
     */
    public List<String> findAllByAddressId(String addressId){
        RealTimeDmAddress realTimeDmAddress = this.findByAddressId(addressId);
        List<String> realTimeDmAddresses = new ArrayList<>();
        if( null != realTimeDmAddress && !StringUtils.isEmpty(realTimeDmAddress.getParentId())){
            List<RealTimeDmAddress> realTimeDmAddressList=  this.findByParentId(realTimeDmAddress.getParentId());
            if( null != realTimeDmAddressList && realTimeDmAddressList.size() > 0){
                for(RealTimeDmAddress realTimeDmAddress1 : realTimeDmAddressList){
                    realTimeDmAddresses.add(realTimeDmAddress1.getAddressId());
                }
            }
        }else{
            realTimeDmAddresses.add(addressId);
        }
        return realTimeDmAddresses;
    }

    public void deleteById(String id){
        RealTimeDmAddress realTimeDmAddress = this.findById(id);
        if( null != realTimeDmAddress ){
            realTimeDmAddressRepository.delete(this.findByParentId(id));
            realTimeDmAddressRepository.delete(id);
        }
    }

    public Page<RealTimeDmAddress> findAll(int page, int pageSize){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, pageSize, sort);
        return realTimeDmAddressRepository.findByNameIsNotNull(pageRequest);
    }

    public List<String> findAllAddressId(){
        List<String> addressIdList = new ArrayList<>();
        List<RealTimeDmAddress> realTimeDmAddressList = realTimeDmAddressRepository.findByAddressIdIsNotNull();
        if( null != realTimeDmAddressList && realTimeDmAddressList.size() > 0){
            for( RealTimeDmAddress realTimeDmAddress :  realTimeDmAddressList){
                addressIdList.add(realTimeDmAddress.getAddressId());
            }
        }
        return addressIdList;
    }

}
