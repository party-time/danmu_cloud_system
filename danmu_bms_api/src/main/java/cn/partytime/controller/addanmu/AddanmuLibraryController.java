package cn.partytime.controller.addanmu;

import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.logic.danmu.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.danmu.AdDanmuLibrary;
import cn.partytime.service.BmsAdDanmuService;
import cn.partytime.service.adDanmu.AdDanmuLibraryService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/1/16.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/adDanmuLibrary")
public class AddanmuLibraryController extends BaseAdminController {


    @Autowired
    private BmsAdDanmuService bmsAdDanmuService;

    @Autowired
    private AdDanmuLibraryService adDanmuLibraryService;



    @Value("${adTimerDanmu.adfilePath}")
    private String adfilePath;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResultModel list(Integer pageNumber, Integer pageSize,int flg) {
        pageNumber = pageNumber -1;
        /*return bmsPartyService.findAllByPage(pageNumber, pageSize,type);*/
        return  bmsAdDanmuService.findAdDanmuLibraryPageResultModel(pageNumber,pageSize,flg);
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id) {
        bmsAdDanmuService.deleteAdDanmuLibrary(id,getAdminUser().getId());

        //删除广告弹幕文件
        File file = new File(adfilePath+File.separator+id);
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public RestResultModel find(String id) {
        AdDanmuLibrary danmuLibrary = adDanmuLibraryService.findAdDanmuLibraryById(id);
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        restResultModel.setData(danmuLibrary);
        return restResultModel;
    }


    @RequestMapping(value = "/recovery", method = RequestMethod.GET)
    public RestResultModel recovery(String id) {
        bmsAdDanmuService.recoveryAdDanmuLibrary(id,getAdminUser().getId());
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        return restResultModel;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel partyList(String name,String libraryId) {
        RestResultModel restResultModel = new RestResultModel();
        AdDanmuLibrary adDanmuLibrary = adDanmuLibraryService.findByName(name);
        if(StringUtils.isEmpty(libraryId)){
            if(adDanmuLibrary!=null){
                restResultModel.setResult(400);
                restResultModel.setResult_msg("名称重复!");
                return restResultModel;
            }
            bmsAdDanmuService.insertAdDanmuLibrary(name,getAdminUser().getId());
        }else{
            if(adDanmuLibrary!=null){
                if(libraryId.equals(adDanmuLibrary.getId())) {
                    bmsAdDanmuService.updateAdDanmuLibrary(libraryId, name, getAdminUser().getId());
                }else{
                    restResultModel.setResult(400);
                    restResultModel.setResult_msg("名称重复!");
                    return restResultModel;
                }
            }else{
                bmsAdDanmuService.updateAdDanmuLibrary(libraryId, name, getAdminUser().getId());
            }
        }
        restResultModel.setResult(200);
        return restResultModel;
    }

}
