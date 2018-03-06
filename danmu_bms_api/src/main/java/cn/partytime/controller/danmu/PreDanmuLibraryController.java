package cn.partytime.controller.danmu;

import cn.partytime.model.PageResultModel;
import cn.partytime.service.danmu.BmsPreDanmuLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 2018/3/6.
 */

@Slf4j
@RestController
@RequestMapping(value = "/v1/api/admin/preDanmuLibrary")
public class PreDanmuLibraryController {

    @Autowired
    private BmsPreDanmuLibraryService bmsPreDanmuLibraryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResultModel list(String name,Integer pageNumber, Integer pageSize) {
        pageNumber = pageNumber -1;
        return bmsPreDanmuLibraryService.findAllByPage(pageNumber,pageSize,name);
    }


}
