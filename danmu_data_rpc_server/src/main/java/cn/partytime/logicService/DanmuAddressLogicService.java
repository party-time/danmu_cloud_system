package cn.partytime.logicService;

import cn.partytime.common.constants.DistanceConst;
import cn.partytime.common.util.DistanceUtil;
import cn.partytime.common.util.ListUtils;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.service.address.DanmuAddressService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lENOVO on 2016/10/24.
 */
@RestController
@RequestMapping("/danmuAddressLogic")
public class DanmuAddressLogicService {

    private static final Logger logger = LoggerFactory.getLogger(DanmuAddressLogicService.class);
    @Autowired
    private DanmuAddressService danmuAddressService;



    @RequestMapping(value = "/findAddressByLonLat" ,method = RequestMethod.GET)
    public DanmuAddress findAddressByLonLat(@RequestParam Double longitude, @RequestParam Double latitude) {

        List<String> addressList = danmuAddressService.findAddressIdList(longitude, latitude, DistanceConst.WEChAT_CLIENT_DISTANCE);
        List<DanmuAddress> resultList = new ArrayList<DanmuAddress>();
        if (ListUtils.checkListIsNotNull(addressList)) {
            List<DanmuAddress> danmuAddressList = danmuAddressService.findDanmuAddressByIdList(addressList);
            for (DanmuAddress danmuAddress : danmuAddressList) {

                logger.info("场地信息:{}", JSON.toJSONString(danmuAddress));
                List<Double> coordinates = danmuAddress.getLocation().getCoordinates();
                Double danmuParty_longitude = coordinates.get(0);
                Double danmuParty_latitude = coordinates.get(1);
                int range = danmuAddress.getRange();
                double distance = DistanceUtil.getDistance(latitude, longitude, danmuParty_latitude, danmuParty_longitude);
                logger.info("活动指定的范围:{},当前用户距离活动地点：{}",range,distance);
                if (range > distance) {
                    resultList.add(danmuAddress);
                }
            }
        }
        if (ListUtils.checkListIsNotNull(resultList)) {
            return resultList.get(0);
        }
        return null;
    }



}
