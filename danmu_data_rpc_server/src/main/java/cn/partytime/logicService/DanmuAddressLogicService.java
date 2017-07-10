package cn.partytime.logicService;

import cn.partytime.common.constants.DistanceConst;
import cn.partytime.common.util.DistanceUtil;
import cn.partytime.common.util.ListUtils;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.DanmuAddressService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 2017/7/10.
 */

@Service
public class DanmuAddressLogicService {

    private static final Logger logger = LoggerFactory.getLogger(DanmuAddressLogicService.class);
    @Autowired
    private DanmuAddressService danmuAddressService;

    @Autowired
    private RedisService redisService;


    public DanmuAddress findAddressByLonLat(Double longitude, Double latitude) {

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
