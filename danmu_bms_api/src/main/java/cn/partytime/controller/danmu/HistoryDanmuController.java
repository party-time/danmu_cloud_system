package cn.partytime.controller.danmu;

import cn.partytime.common.util.ComponentKeyConst;
import cn.partytime.common.util.FileUtils;
import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.danmu.Danmu;
import cn.partytime.model.danmu.DanmuPool;
import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechatMsgTmpl.MsgTmpl;
import cn.partytime.model.wechatMsgTmpl.ValueTmpl;
import cn.partytime.service.*;
import cn.partytime.service.wechat.WechatUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lENOVO on 2016/10/17.
 */

@RestController
@RequestMapping(value = "/v1/api")
public class HistoryDanmuController extends BaseAdminController {

    @Autowired
    private BmsDanmuService bmsDanmuService;

    @Autowired
    private DanmuService danmuService;

    @Autowired
    private BmsPrizeService bmsPrizeService;

    @Autowired
    private BmsHistoryDanmuService bmsHistoryDanmuService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private DanmuPoolService danmuPoolService;

    @Autowired
    private PartyAddressRelationService partyAddressRelationService;

    @Autowired
    private BmsWechatUserService bmsWechatUserService;

    @Value("${sendGift.id}")
    private String sendGiftId;



    @RequestMapping(value = "/admin/historyDanmu/page", method = RequestMethod.GET)
    @ResponseBody
    public PageResultModel historyDMList(Integer pageNumber, Integer pageSize, String arrayArea, String partyId) {
        return bmsDanmuService.findPageResultModel(pageNumber - 1, pageSize, arrayArea, partyId, 1);
    }

    /**
     * 发送奖品
     *
     * @return
     */
    @RequestMapping(value = "/admin/historyDanmu/addPrize", method = RequestMethod.GET)
    @ResponseBody
    public RestResultModel prize(String danmuId, String openId,String partyId, HttpServletRequest request) {
        String userId = getAdminUser().getId();
        return bmsPrizeService.sendPrize(partyId,openId,danmuId,userId);
    }


    @RequestMapping(value = "/admin/historyDanmu/block", method = RequestMethod.GET)
    @ResponseBody
    public RestResultModel blockDanmu(String id) {
        RestResultModel restResultModel = new RestResultModel();

        danmuService.blockDanmu(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping("/admin/historyDanmu/download")
    public String download(String fileName, HttpServletRequest request, HttpServletResponse response, String addressArea, String partyId) {
        InputStream inputStream =null;
        OutputStream os =null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + partyId+".xls");
        String path=bmsHistoryDanmuService.historyDanmuExport(partyId,addressArea);
        if(StringUtils.isEmpty(path)){
            return null;
        }
        try {
            inputStream = new FileInputStream(new File(path));
            os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }

            // 这里主要关闭。
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileUtils.deleteDir(new File(path));
        }
        return null;
    }

    @RequestMapping(value = "/admin/historyDanmu/sendGift", method = RequestMethod.GET)
    @ResponseBody
    public RestResultModel sendGift(String id, String msg) {
        RestResultModel restResultModel = new RestResultModel();
        Danmu danmuModel = danmuService.findById(id);
        if( null != danmuModel){
            WechatUser wechatUser = wechatUserService.findByUserId(danmuModel.getCreateUserId());
            DanmuPool danmuPool = danmuPoolService.findById(danmuModel.getDanmuPoolId());
            PartyAddressRelation partyAddressRelation =null;
            if( null != danmuPool){
                partyAddressRelation = partyAddressRelationService.findById(danmuPool.getPartyAddressRelationId());
            }
            if( null != wechatUser && null != partyAddressRelation){
                Map<String,String> map = new HashMap<>();
                map.put("userName",wechatUser.getNick());
                map.put("gift",msg);
                bmsDanmuService.sendDanmuByWechat(ComponentKeyConst.SEND_GIFT, map, wechatUser.getOpenId(), partyAddressRelation.getPartyId(), partyAddressRelation.getAddressId(), 0, 0);
                MsgTmpl msgTmpl = new MsgTmpl();
                msgTmpl.setTouser(wechatUser.getOpenId());
                msgTmpl.setTemplate_id(sendGiftId);
                msgTmpl.setTopcolor("#FF0000");
                msgTmpl.setUrl("");
                Map<String,ValueTmpl> content = new HashMap<String,ValueTmpl>();
                ValueTmpl valueTmpl = new ValueTmpl();
                valueTmpl.setValue("恭喜您中奖了");
                content.put("firsts",valueTmpl);
                ValueTmpl valueTmpl1 = new ValueTmpl();
                valueTmpl1.setValue("您的答案非常好");
                content.put("keyword1",valueTmpl1);
                ValueTmpl valueTmpl2 = new ValueTmpl();
                valueTmpl2.setValue(msg);
                content.put("keyword2",valueTmpl2);
                ValueTmpl valueTmpl3 = new ValueTmpl();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                valueTmpl3.setValue(sdf.format(new Date()));
                content.put("keyword3",valueTmpl3);
                ValueTmpl valueTmpl4 = new ValueTmpl();
                valueTmpl4.setValue("请尽快找工作人员领取电影票");
                content.put("remark",valueTmpl4);
                msgTmpl.setData(content);
                bmsWechatUserService.sendMsg(msgTmpl);
            }
        }
        restResultModel.setResult(200);
        return restResultModel;
    }


}
