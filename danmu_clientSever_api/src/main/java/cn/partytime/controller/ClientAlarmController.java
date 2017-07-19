package cn.partytime.controller;

import cn.partytime.common.constants.AlarmConst;
import cn.partytime.model.RestResultModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dm on 2017/7/18.
 */

@RestController
@RequestMapping(value = "/v1/api/javaClient")
public class ClientAlarmController {

    @RequestMapping(value = "/alarm/{code}/{type}", method = RequestMethod.GET)
    public RestResultModel danmuNotice(@PathVariable("code") String code,@PathVariable("type") String type){
        switch (type){
            case AlarmConst.DanmuAlarmType.DANMU_IS_NULL:
                System.out.println("");
            case AlarmConst.DanmuAlarmType.HISTORY_DANMU_IS_NULL:
                System.out.println("");
            case AlarmConst.DanmuAlarmType.TIMER_DANMU_IS_NULL:
                System.out.println("");
            case AlarmConst.DanmuAlarmType.DANMU_IS_MORE:
        }

        return null;
    }

    private void sendMessage(){

    }


}
