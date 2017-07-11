package cn.partytime.service;

import cn.partytime.model.manager.danmuCmdJson.CmdComponentValue;
import cn.partytime.service.danmuCmdJson.CmdComponentValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lENOVO on 2016/10/19.
 */

@Service
public class BmsColorService {

    @Autowired
    private CmdComponentValueService cmdComponentValueService;


    /**
     * 获取弹幕颜色
     *type null 数据库状态  0将0x转换称#号
     * @return
     */
    public List<String> findDanmuColor(Integer type) {
        List<CmdComponentValue> cmdComponentValueList = cmdComponentValueService.findByComponentId("591d0a210cf2ba0165c4f83f");
        List<String> colorList = new ArrayList<String>();
        for(CmdComponentValue cmdComponentValue : cmdComponentValueList){
            if( null == type){
                colorList.add(cmdComponentValue.getValue());
            }else{
                colorList.add(cmdComponentValue.getValue().replaceAll("0x","#"));
            }

        }
        return colorList;
    }

    public String getRandomColor(){
        List<CmdComponentValue> cmdComponentValueList = cmdComponentValueService.findByComponentId("591d0a210cf2ba0165c4f83f");
        if( null != cmdComponentValueList){
            Random r = new Random();
            int a = r.nextInt(cmdComponentValueList.size()-1);
            return cmdComponentValueList.get(a).getValue();
        }else{
            return "0xffffff";
        }
    }

}
