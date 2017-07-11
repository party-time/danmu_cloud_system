package cn.partytime.controller;

import cn.partytime.model.DanmuPoolListResult;
import cn.partytime.model.RestResultModel;
import cn.partytime.service.DanmuPoolListResultService;
import cn.partytime.service.DanmuPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuwei on 2016/9/5.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/danmuPool")
@Slf4j
public class DanmuPoolController {

    @Autowired
    private DanmuPoolService danmuPoolService;

    @Autowired
    private DanmuPoolListResultService danmuPoolListResultService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RestResultModel danmuPoolList(String partyId ) {
        RestResultModel restResultModel = new RestResultModel();
        List<DanmuPoolListResult> danmuPoolList = danmuPoolListResultService.findByPartyId(partyId);
        restResultModel.setResult(200);
        restResultModel.setData(danmuPoolList);
        return restResultModel;
    }

    @RequestMapping(value = "/searchDanmuPool", method = RequestMethod.GET)
    public RestResultModel otherDanmuPoolList(String partyName, String danmuPoolId) {
        RestResultModel restResultModel = new RestResultModel();
        List<DanmuPoolListResult> danmuPoolList = danmuPoolListResultService.findByPartyNameLike(partyName);
        List<DanmuPoolListResult> danmuPoolListTemp = new ArrayList<DanmuPoolListResult>();
        if( null != danmuPoolList && danmuPoolList.size() > 0) {
            danmuPoolListTemp.addAll(danmuPoolList);
            int i = 0;
            for (DanmuPoolListResult danmuPoolListResult : danmuPoolList) {
                for (String di : danmuPoolListResult.getDanmuPoolIdList()) {
                    if (danmuPoolId.equals(di)) {
                        danmuPoolListTemp.remove(i);
                    }
                }
                ++i;
            }
        }
        restResultModel.setResult(200);
        restResultModel.setData(danmuPoolListTemp);
        return restResultModel;
    }

    @RequestMapping(value = "/mergeDanmuPool", method = RequestMethod.GET)
    public RestResultModel mergeDanmuPool(String danmuPoolId, String mergeDanmuPoolId ) {
        RestResultModel restResultModel = new RestResultModel();
        danmuPoolListResultService.mergeDanmuPool(danmuPoolId,mergeDanmuPoolId);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/cancelMerge", method = RequestMethod.GET)
    public RestResultModel cancelMergeDanmuPool(String danmuPoolId) {
        RestResultModel restResultModel = new RestResultModel();
        danmuPoolListResultService.cancelMergeDanmuPool(danmuPoolId);
        restResultModel.setResult(200);
        return restResultModel;
    }
}
