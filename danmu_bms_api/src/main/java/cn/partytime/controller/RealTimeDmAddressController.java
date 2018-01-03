package cn.partytime.controller;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.RealTimeDmAddress;
import cn.partytime.service.RealTimeDmAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by administrator on 2018/1/3.
 */
public class RealTimeDmAddressController {

    @Autowired
    private RealTimeDmAddressService realTimeDmAddressService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResultModel realTimeDmAddressList(Integer pageNumber, Integer pageSize) {
        Page<RealTimeDmAddress> rtdaPage = realTimeDmAddressService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setRows(rtdaPage.getContent());
        pageResultModel.setTotal(rtdaPage.getTotalElements());
        return pageResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel saveRealTimeDmAddress() {
        RestResultModel restResultModel = new RestResultModel();


        return restResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public RestResultModel getRealTimeDmAddress() {
        RestResultModel restResultModel = new RestResultModel();


        return restResultModel;
    }



    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public RestResultModel deleteRealTimeDmAddress(String id) {
        RestResultModel restResultModel = new RestResultModel();
        realTimeDmAddressService.deleteById(id);
        return restResultModel;
    }


}
