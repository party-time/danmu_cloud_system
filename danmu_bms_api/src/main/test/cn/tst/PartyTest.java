package cn.tst;

import cn.partytime.Application;
import cn.partytime.common.util.DateUtils;
import cn.partytime.model.manager.Party;
import cn.partytime.service.PartyService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by Administrator on 2017/1/23.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class PartyTest {


    @Autowired
    private PartyService partyService;

    @Test
    public void findParty(){
        //Page<Party> partyList = partyService.findAllByPage(0,10);
        //List<Party>  partyList =  partyService.findPartyByTime(DateUtils.getCurrentDate(),0);
        //List<Party> partyList1 = partyList.getContent();
        //for(Party party:partyList){
          //  System.out.println(JSON.toJSONString(party));
        //}
        Party party = partyService.findById("5885774bda4fba36f84ca966");


    }
}
