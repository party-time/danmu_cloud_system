package cn.partytime.controller;

import cn.partytime.logic.danmu.PageResultModel;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.LocationModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.service.BmsAddressService;
import cn.partytime.service.DanmuAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwei on 2016/9/1.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/address")
@Slf4j
public class AddressController {

    @Autowired
    private DanmuAddressService danmuAddressService;


    @Autowired
    private BmsAddressService bmsAddressService;


    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel addressPage(Integer pageNumber , Integer pageSize ){
        pageNumber = pageNumber-1;
        Page<DanmuAddress> addressPage = danmuAddressService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(addressPage.getTotalElements());
        pageResultModel.setRows(addressPage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(@ModelAttribute DanmuAddress address, HttpServletRequest request){
        RestResultModel restResultModel = new RestResultModel();
        LocationModel locationModel = new LocationModel();
        List<Double> longilatiList = new ArrayList<Double>();

        double longitude = Double.parseDouble(request.getParameter("longitude")+"");
        double latitude = Double.parseDouble(request.getParameter("latitude")+"");

        if(!bmsAddressService.checkAddressIsNotExist(longitude,latitude,address.getRange())){
            restResultModel.setResult(500);
            return restResultModel;
        }

        longilatiList.add(longitude);
        longilatiList.add(latitude);
        locationModel.setCoordinates(longilatiList);
        locationModel.setType("Point");
        address.setLocation(locationModel);

        if(StringUtils.isEmpty(address.getId())){
            address.setId(null);
            danmuAddressService.save(address);
        }else{
            danmuAddressService.update(address);
        }

        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id ){
        RestResultModel restResultModel = new RestResultModel();
        danmuAddressService.deleteById(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public RestResultModel query(String id ){
        RestResultModel restResultModel = new RestResultModel();
        DanmuAddress danmuAddress = danmuAddressService.findById(id);
        if( null != danmuAddress){
            restResultModel.setResult(200);
            restResultModel.setData(danmuAddress);
        }else{
            restResultModel.setResult(404);
        }

        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(@ModelAttribute DanmuAddress address,HttpServletRequest request){
        RestResultModel restResultModel = new RestResultModel();
        LocationModel locationModel = new LocationModel();
        List<Double> longilatiList = new ArrayList<Double>();
        double longitude = Double.parseDouble(request.getParameter("longitude")+"");
        double latitude = Double.parseDouble(request.getParameter("latitude")+"");
        longilatiList.add(longitude);
        longilatiList.add(latitude);

        if(!bmsAddressService.checkAddressIsExist(longitude,latitude,address.getId(),address.getRange())){
            restResultModel.setResult(500);
            return restResultModel;
        }
        locationModel.setCoordinates(longilatiList);
        locationModel.setType("Point");
        address.setLocation(locationModel);
        danmuAddressService.updateById(address);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/queryNoParty", method = RequestMethod.GET)
    public PageResultModel queryNoParty(String partyId , Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber-1;
        Page<DanmuAddress> danmuAddressPage = danmuAddressService.findAddressNotEqualsPartyId(partyId,pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setRows(danmuAddressPage.getContent());
        pageResultModel.setTotal(danmuAddressPage.getTotalElements());
        return pageResultModel;
    }

    @RequestMapping(value = "/queryByPartyId", method = RequestMethod.GET)
    public PageResultModel queryByPartyId(String partyId,Integer pageNumber, Integer pageSize ){
        pageNumber = pageNumber-1;
        return  bmsAddressService.findAddressByPartyId(partyId,pageNumber,pageSize);
    }


    @RequestMapping(value = "/getByPartyId", method = RequestMethod.GET)
    public RestResultModel getByPartyId(String partyId){
        RestResultModel restResultModel = new RestResultModel();
        List<DanmuAddress> danmuAddressList = danmuAddressService.findAddressByPartyId(partyId);
        restResultModel.setData(danmuAddressList);
        restResultModel.setResult(200);
        return  restResultModel;
    }

    @RequestMapping(value = "/queryAll", method = RequestMethod.GET)
    public PageResultModel queryAll(String addressIds,Integer pageNumber, Integer pageSize ){
        pageNumber = pageNumber-1;

        if(StringUtils.isEmpty(addressIds)){
            return  new PageResultModel(danmuAddressService.findAll(pageNumber,pageSize));
        }else{
            List<String> addressIdList = new ArrayList<>();
            if(addressIds.indexOf(",") != -1){
                String[] addressIdStrs = addressIds.split(",");
                for( String addressId : addressIdStrs){
                    addressIdList.add(addressId);
                }
            }else{
                addressIdList.add(addressIds);
            }
            return  new PageResultModel(danmuAddressService.findAddressNotIn(addressIdList,pageNumber,pageSize));
        }


    }


    @RequestMapping(value = "/queryAdByPartyId", method = RequestMethod.GET)
    public PageResultModel queryAdByPartyId(String partyId , Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber-1;
        return bmsAddressService.findAdByPartyId(partyId,pageNumber,pageSize);
    }

    @RequestMapping(value = "/getPageByPartyId", method = RequestMethod.GET)
    public PageResultModel getPageByPartyId(String partyId,Integer pageNumber, Integer pageSize  ){
        pageNumber = pageNumber-1;
        PageResultModel pageResultModel = new PageResultModel(danmuAddressService.findDanmuAddressByPartyId(partyId,pageNumber,pageSize));

        return  pageResultModel;
    }

    @RequestMapping(value = "/setAddressControle", method = RequestMethod.GET)
    public RestResultModel setAddressControle(String addressId,String keys){
        RestResultModel restResultModel = new RestResultModel();
        Map<String,Integer> map = new HashMap<>();
        for(String key:keys.split(",")){
            map.put(key,1);
        }
        danmuAddressService.updateControlerStatus(addressId,map);
        restResultModel.setResult(200);
        return  restResultModel;
    }


}
