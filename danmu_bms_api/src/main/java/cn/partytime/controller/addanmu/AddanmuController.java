package cn.partytime.controller.addanmu;

import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.service.BmsAdDanmuService;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/1/16.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/adDanmu")
public class AddanmuController extends BaseAdminController {


    @Autowired
    private BmsAdDanmuService bmsAdDanmuService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResultModel partyList(Integer pageNumber, Integer pageSize, String libraryId) {
        pageNumber = pageNumber -1;
        return bmsAdDanmuService.findAdPageResultModel(pageNumber, pageSize,libraryId);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(HttpServletRequest request) {
        RestResultModel restResultModel = new RestResultModel();
        bmsAdDanmuService.saveAdDanmu(request,getAdminUser().getId());
        restResultModel.setResult(200);
        return restResultModel;
    }



    @RequestMapping(value = "/saveVideo", method = RequestMethod.POST)
    public RestResultModel saveVideo(HttpServletRequest request) {
        RestResultModel restResultModel = new RestResultModel();

        String videoId = request.getParameter("videoId");
        if(StringUtil.isBlank(videoId)){
            restResultModel.setResult(400);
            restResultModel.setResult_msg("请选择视频");
            return restResultModel;
        }
        bmsAdDanmuService.saveVideo(request,getAdminUser().getId());
        restResultModel.setResult(200);
        return restResultModel;
    }


    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String danmuId) {
        RestResultModel restResultModel =new RestResultModel();
        bmsAdDanmuService.deleteAdDanmu(danmuId,getAdminUser().getId());
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public PageResultModel update(Integer pageNumber, Integer pageSize, Integer type) {
        //pageNumber = pageNumber -1;
        //return bmsPartyService.findAllByPage(pageNumber, pageSize,type);
        return null;
    }

    @RequestMapping(value = "/createFile", method = RequestMethod.GET)
    public RestResultModel createFile(String libraryId) {
        RestResultModel restResultModel =new RestResultModel();
        bmsAdDanmuService.createTimerFile(libraryId,getAdminUser().getId());
        restResultModel.setResult(200);
        return restResultModel;
    }



}
