package cn.partytime.service;

import cn.partytime.model.manager.RealTimeDmAddress;
import cn.partytime.repository.manager.RealTimeDmAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
                    this.save(realTimeDmAddress);
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

}
