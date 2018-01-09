package cn.partytime.controller;

import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.DeviceIpInfo;
import cn.partytime.service.DeviceIpInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by administrator on 2017/3/21.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/device")
@Slf4j
public class DeviceIpInfoController {

    @Autowired
    private DeviceIpInfoService deviceIpInfoService;

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public RestResultModel findByaddressId( String addressId){
        RestResultModel restResultModel = new RestResultModel();
        List<DeviceIpInfo> deviceIpInfoList = deviceIpInfoService.findAllByAddressId(addressId);
        restResultModel.setResult(200);
        restResultModel.setData(deviceIpInfoList);
        return restResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST,consumes = "application/json")
    public RestResultModel save(@RequestBody(required = false)List<DeviceIpInfo> deviceIpInfoList){
        RestResultModel restResultModel = new RestResultModel();
        deviceIpInfoService.save(deviceIpInfoList);
        restResultModel.setResult(200);
        restResultModel.setData(deviceIpInfoList);
        return restResultModel;
    }




}
