package cn.partytime.controller.clientControl;

import cn.partytime.model.RestResultModel;
import cn.partytime.service.BmsClientWechatSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/4/6.
 */
@RestController
@RequestMapping(value = "/v1/api/printScreen")
@Slf4j
public class PrintScreenController {

    @Autowired
    private BmsClientWechatSendService bmsClientWechatSendService;

    @RequestMapping(value = "/sendScreenPic", method = RequestMethod.GET)
    public RestResultModel sendScreenPic(String addressId, String adminId, String num){
        RestResultModel restResultModel = new RestResultModel();
        bmsClientWechatSendService.sendScreenPic(addressId,adminId);
        restResultModel.setResult(200);
        return restResultModel;
    }
}
