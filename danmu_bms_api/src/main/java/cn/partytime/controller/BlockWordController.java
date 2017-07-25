package cn.partytime.controller;

import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.manager.BlockKeyword;
import cn.partytime.model.RestResultModel;
import cn.partytime.service.BlockKeywordService;
import cn.partytime.service.BmsBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by lENOVO on 2016/8/25.
 */

@RestController
@RequestMapping(value = "/v1/api")
public class BlockWordController extends BaseAdminController {

    @Resource(name = "blockKeywordService")
    private BlockKeywordService blockKeywordService;

    @Autowired
    private BmsBlockService bmsBlockService;





    @RequestMapping(value = "/admin/blockKeywords", method = RequestMethod.GET)
    public PageResultModel getAdminBlockKeywords(int pageNumber, int pageSize) {
        pageNumber = pageNumber-1;
        Page<BlockKeyword> blockKeywordPage = blockKeywordService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(blockKeywordPage.getTotalElements());
        pageResultModel.setRows(blockKeywordPage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/admin/blockKeywords/find", method = RequestMethod.GET)
    public PageResultModel getAdminBlockKeywords(String word , int pageNumber, int pageSize) {
        pageNumber = pageNumber-1;
        Page<BlockKeyword> blockKeywordPage = blockKeywordService.findByWordLike(word,pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(blockKeywordPage.getTotalElements());
        pageResultModel.setRows(blockKeywordPage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/admin/blockKeywords/{id}", method = RequestMethod.DELETE)
    public RestResultModel delAdminBlockKeyword(@PathVariable String id) {
        RestResultModel restResultModel = new RestResultModel();
        if (StringUtils.isEmpty(id)) {
            restResultModel.setResult(401);
            restResultModel.setResult_msg("id is empty");
        } else {
            try {
                bmsBlockService.deleteBlockWord(id);
                restResultModel.setResult(200);
                restResultModel.setResult_msg("ok");
            } catch (Exception e) {
                restResultModel.setResult(402);
                restResultModel.setResult_msg("error");
            }
        }
        return restResultModel;
    }


    @RequestMapping(value = "/admin/blockKeywords", method = RequestMethod.POST)
    public RestResultModel addAdminBlockKeyword(@RequestParam String word, HttpServletRequest request) {
        RestResultModel restResultModel = new RestResultModel();
        if (StringUtils.isEmpty(word)) {
            restResultModel.setResult(401);
            restResultModel.setResult_msg("id is empty");
        } else {
            try {
                String userId = getAdminUser().getId();
                BlockKeyword blockKeyword = bmsBlockService.addBlockWord(word, userId);
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
