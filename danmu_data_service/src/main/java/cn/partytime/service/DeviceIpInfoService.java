package cn.partytime.service;

import cn.partytime.model.manager.DeviceIpInfo;
import cn.partytime.repository.manager.DeviceIpInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2017/3/21.
 */
@Service
@Slf4j
public class DeviceIpInfoService {

    @Autowired
    private DeviceIpInfoRepository deviceIpInfoRepository;

    public DeviceIpInfo save(String addressId,String ip,Integer port,Integer type){
        DeviceIpInfo deviceIpInfo = new DeviceIpInfo();
        deviceIpInfo.setAddressId(addressId);
        deviceIpInfo.setIp(ip);
        deviceIpInfo.setPort(port);
        deviceIpInfo.setType(type);
        return deviceIpInfoRepository.save(deviceIpInfo);
    }

    public List<DeviceIpInfo> findByAddressId(String addressId){
        List<DeviceIpInfo> deviceIpInfoList = deviceIpInfoRepository.findByAddressId(addressId);
        if( null != deviceIpInfoList){
            List<DeviceIpInfo> dipList = new ArrayList<>();
            for(DeviceIpInfo deviceIpInfo : deviceIpInfoList){
                if(!StringUtils.isEmpty(deviceIpInfo.getIp())){
                    dipList.add(deviceIpInfo);
                }
            }
            return dipList;
        }else{
            return deviceIpInfoList;
        }

    }

    public void save(List<DeviceIpInfo> deviceIpInfos){
        deviceIpInfoRepository.save(deviceIpInfos);
    }

    public List<DeviceIpInfo> findByAddressIdAndType(String addressId,Integer type){
        return deviceIpInfoRepository.findByAddressIdAndType(addressId,type);
    }

}
