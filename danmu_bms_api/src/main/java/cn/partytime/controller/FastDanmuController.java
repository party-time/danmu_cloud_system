package cn.partytime.controller;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.FastDanmu;
import cn.partytime.service.FastDanmuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/v1/api/admin")
public class FastDanmuController {

    @Autowired
    private FastDanmuService fastDanmuService;

    @RequestMapping(value = "/fastdm/page", method = RequestMethod.GET)
    public PageResultModel getByPartyId(String partyId,int pageNumber, int pageSize) {
        pageNumber = pageNumber-1;
        Page<FastDanmu> blockKeywordPage = fastDanmuService.findByPartyId(partyId,pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(blockKeywordPage.getTotalElements());
        pageResultModel.setRows(blockKeywordPage.getContent());
        return pageResultModel;
    }



    @RequestMapping(value = "/fastdm/find", method = RequestMethod.GET)
    public RestResultModel getAdminBlockKeywords(String word , String partyId) {
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setData(fastDanmuService.findByPartyIdAndWord(partyId,word));
        restResultModel.setResult(200);
        return restResultModel;
    }



    @RequestMapping(value = "/fastdm/{id}", method = RequestMethod.DELETE)
    public RestResultModel delAdminBlockKeyword(@PathVariable String id) {
        RestResultModel restResultModel = new RestResultModel();
        if (StringUtils.isEmpty(id)) {
            restResultModel.setResult(401);
            restResultModel.setResult_msg("id is empty");
        } else {
            try {
                fastDanmuService.delete(id);
                restResultModel.setResult(200);
                restResultModel.setResult_msg("ok");
            } catch (Exception e) {
                restResultModel.setResult(402);
                restResultModel.setResult_msg("error");
            }
        }
        return restResultModel;
    }


    @RequestMapping(value = "/fastdm", method = RequestMethod.POST)
    public RestResultModel addAdminBlockKeyword(@RequestParam String word,
                                                @RequestParam String partyId,HttpServletRequest request) {
        RestResultModel restResultModel = new RestResultModel();
        if (StringUtils.isEmpty(word)) {
            restResultModel.setResult(401);
            restResultModel.setResult_msg("id is empty");
        } else {
            try {
                FastDanmu fdm = new FastDanmu();
                fdm.setWord(word);
                fdm.setPartyId(partyId);
                FastDanmu blockKeyword = fastDanmuService.save(fdm);
                if (blockKeyword != null) {
                    restResultModel.setResult(200);
                    restResultModel.setResult_msg("ok");
                } else {
                    restResultModel.setResult(401);
                    restResultModel.setResult_msg("fail");
                }
            } catch (Exception e) {
                restResultModel.setResult(402);
                restResultModel.setResult_msg("error");
            }
        }
        return restResultModel;
    }
}
