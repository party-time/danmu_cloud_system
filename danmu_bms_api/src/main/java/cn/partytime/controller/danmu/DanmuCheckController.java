package cn.partytime.controller.danmu;

import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.danmuCmdJson.CmdTemp;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dm on 2017/6/2.
 */

@RestController
@RequestMapping(value = "/v1/api/admin")
public class DanmuCheckController {

    @RequestMapping(value = "/fileDanmuCheck", method = RequestMethod.GET)
    public RestResultModel findDanmuType(Integer pageNumber, Integer pageSize) {
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        restResultModel.setResult_msg("OK");
        return restResultModel;
    }
}
