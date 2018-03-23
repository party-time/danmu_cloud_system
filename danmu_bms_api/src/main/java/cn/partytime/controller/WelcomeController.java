package cn.partytime.controller;

import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.welcome.Welcome;
import cn.partytime.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/v1/api/admin/welcome")
public class WelcomeController extends BaseAdminController {

    @Autowired
    private WelcomeService welcomeService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel getAdminBlockKeywords(int pageNumber, int pageSize) {
        pageNumber = pageNumber-1;
        Page<Welcome> welcomePage = welcomeService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(welcomePage.getTotalElements());
        pageResultModel.setRows(welcomePage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public PageResultModel getAdminBlockKeywords(String word , int pageNumber, int pageSize) {
        pageNumber = pageNumber-1;
        Page<Welcome> blockKeywordPage = welcomeService.findByWordLike(word,pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(blockKeywordPage.getTotalElements());
        pageResultModel.setRows(blockKeywordPage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/del/{id}", method = RequestMethod.DELETE)
    public RestResultModel delAdminBlockKeyword(@PathVariable String id) {
        RestResultModel restResultModel = new RestResultModel();
        if (StringUtils.isEmpty(id)) {
            restResultModel.setResult(401);
            restResultModel.setResult_msg("id is empty");
        } else {
            try {
                welcomeService.delete(id);
                restResultModel.setResult(200);
                restResultModel.setResult_msg("ok");
            } catch (Exception e) {
                restResultModel.setResult(402);
                restResultModel.setResult_msg("error");
            }
        }
        return restResultModel;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel addAdminBlockKeyword(@RequestParam String word, HttpServletRequest request) {
        RestResultModel restResultModel = new RestResultModel();
        if (StringUtils.isEmpty(word)) {
            restResultModel.setResult(401);
            restResultModel.setResult_msg("id is empty");
        } else {
            try {
                Welcome welcome = new Welcome();
                welcome.setMessage(word);
                Welcome w1 = welcomeService.save(welcome);
                if (w1 != null) {
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
