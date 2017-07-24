package cn.partytime.service;

import cn.partytime.model.DanmuClientListResult;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.client.DanmuClient;
import cn.partytime.model.manager.RegistCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuwei on 2016/9/8.
 */
@Service
public class DanmuClientListService {
    @Autowired
    private DanmuClientService danmuClientService;
    @Autowired
    private RegistCodeService registCodeService;


    public List<DanmuClientListResult> findByAddressId(String addressId){
        List<DanmuClient> danmuClientList =danmuClientService.findByAddressId(addressId);
        return returnDanmuClientListResult(danmuClientList);
    }


    public List<DanmuClientListResult> returnDanmuClientListResult(List<DanmuClient> danmuClientList){
        List<DanmuClientListResult> danmuClientListResults = new ArrayList<DanmuClientListResult>();

        if( null != danmuClientList && danmuClientList.size() > 0){
            for(DanmuClient danmuClient : danmuClientList){
                RegistCode registCode = registCodeService.findByRegistCode(danmuClient.getRegistCode());
                DanmuClientListResult danmuClientListResult = new DanmuClientListResult();
                danmuClientListResult.setId(danmuClient.getId());
                danmuClientListResult.setName(danmuClient.getName());
                danmuClientListResult.setRegistCode(danmuClient.getRegistCode());
                if( null != registCode && null != registCode.getOverdue()){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String overdueStr = simpleDateFormat.format(registCode.getOverdue());
                    danmuClientListResult.setOverdueStr(overdueStr);
                }else{
                    danmuClientListResult.setOverdueStr("永久有效");
                }
                if(!StringUtils.isEmpty(danmuClient.getDanmuClientCode())){
                    danmuClientListResult.setIsHaveClient(1);
                }else{
                    danmuClientListResult.setIsHaveClient(0);
                }
                danmuClientListResult.setParamTemplateId(danmuClient.getParamTemplateId());
                danmuClientListResults.add(danmuClientListResult);
            }

        }

        return danmuClientListResults;

    }

    public PageResultModel findByAddressId(String addressId, int pageNo , int pageSize){
        Page<DanmuClient> danmuClientPage =danmuClientService.findByAddressId(addressId,pageNo,pageSize);
        PageResultModel pageResultModel = new PageResultModel();

        if( null != danmuClientPage){

        }

        pageResultModel.setTotal(danmuClientPage.getTotalElements());
        pageResultModel.setRows(returnDanmuClientListResult(danmuClientPage.getContent()));
        return pageResultModel;
    }

}
