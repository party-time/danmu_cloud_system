package cn.partytime.controller.versionManager;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.Version;
import cn.partytime.service.versionManager.BmsUpdatePlanService;
import cn.partytime.service.versionManager.VersionService;
import cn.partytime.util.UploadFlashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by administrator on 2017/2/13.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/version")
@Slf4j
public class VersionController {

    @Autowired
    private VersionService versionService;

    @Autowired
    private BmsUpdatePlanService bmsUpdatePlanService;

    @Autowired
    private UploadFlashUtil uploadFlashUtil;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel<Version> findAll(Integer pageSize, Integer pageNumber){
        pageNumber = pageNumber-1;
        Page<Version> versionPage = versionService.findAll(pageNumber,pageSize);
        PageResultModel<Version> pageResultModel = new PageResultModel<>();
        //
        pageResultModel.setRows(versionPage.getContent());
        pageResultModel.setTotal(versionPage.getTotalElements());
        return pageResultModel;
    }

    @RequestMapping(value = "/pageByAddressId", method = RequestMethod.GET)
    public PageResultModel<Version> findByIdNotIn(String addressId,Integer pageSize,Integer pageNumber){
        pageNumber = pageNumber-1;
        return bmsUpdatePlanService.findByAddressIdNotIn(addressId,pageSize,pageNumber);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(String name, String version, String describe, Integer type){
        RestResultModel restResultModel = new RestResultModel();
        if( type==0){
            Integer count = uploadFlashUtil.countJavaFile();
            if( null == count || 0== count){
                restResultModel.setResult(501);
                restResultModel.setResult_msg("没有java客户端");
                return restResultModel;
            }
            if( count > 1){
                restResultModel.setResult(501);
                restResultModel.setResult_msg("有多个java客户端，请删除多余的");
                return restResultModel;
            }
        }else if( type == 1) {
            Integer count = uploadFlashUtil.countFlashFile();
            if (null == count || 0== count) {
                restResultModel.setResult(501);
                restResultModel.setResult_msg("没有flash客户端");
                return restResultModel;
            }
            if (count > 1) {
                restResultModel.setResult(501);
                restResultModel.setResult_msg("有多个flash客户端，请删除多余的");
                return restResultModel;
            }
        }
        uploadFlashUtil.createVersion(type,version);
        versionService.save(name,version,describe,type);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel save(String id){
        RestResultModel restResultModel = new RestResultModel();
        versionService.del(id);
        restResultModel.setResult(200);
        return restResultModel;
    }


    @RequestMapping(value = "/checkVersion", method = RequestMethod.GET)
    public RestResultModel checkVersion(String version,Integer type){
        RestResultModel restResultModel = new RestResultModel();
        List<Version> versionList = versionService.findByVersionAndType(version,type);
        if( null == versionList || versionList.size() ==0){
            restResultModel.setResult(200);
        }else{
            restResultModel.setResult(501);
            restResultModel.setResult_msg("版本号有重复");
        }
        return restResultModel;
    }

    @RequestMapping(value = "/testVersion", method = RequestMethod.GET)
    public RestResultModel testVersion(String version,Integer type){
        RestResultModel restResultModel = new RestResultModel();
        uploadFlashUtil.createVersion(type,version);
        return restResultModel;
    }
}
