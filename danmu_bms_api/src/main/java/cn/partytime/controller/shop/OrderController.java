package cn.partytime.controller.shop;

import cn.partytime.model.PageResultModel;
import cn.partytime.service.shop.BmsOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/7/7.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/order")
@Slf4j
public class OrderController {

    @Autowired
    private BmsOrderService bmsOrderService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel orderPage(Integer pageNumber , Integer pageSize ){
        pageNumber = pageNumber-1;
        return bmsOrderService.findAll(pageNumber,pageSize);
    }


}
