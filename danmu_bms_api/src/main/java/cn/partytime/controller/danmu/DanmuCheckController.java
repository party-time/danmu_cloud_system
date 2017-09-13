package cn.partytime.controller.danmu;

import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.AdminUser;
import cn.partytime.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dm on 2017/6/2.
 */

@RestController
@RequestMapping(value = "/v1/api/admin")
public class DanmuCheckController extends BaseAdminController {

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = "/fileDanmuCheck", method = RequestMethod.GET)
    public RestResultModel findDanmuType(Integer pageNumber, Integer pageSize) {
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        restResultModel.setResult_msg("OK");
        return restResultModel;
    }
}
