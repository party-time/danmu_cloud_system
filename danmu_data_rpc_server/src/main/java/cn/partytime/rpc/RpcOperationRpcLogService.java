package cn.partytime.rpc;

import cn.partytime.logicService.OperationRpcLogService;
import cn.partytime.model.manager.Party;
import cn.partytime.service.AdminUserService;
import cn.partytime.service.DanmuAddressService;
import cn.partytime.service.PartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2018/3/16.
 */

@RestController
@RequestMapping("/rpcOperationRpcLog")
@Slf4j
public class RpcOperationRpcLogService {

    @Autowired
    private OperationRpcLogService operationRpcLogService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = "/insertOperationLogOfParty", method = RequestMethod.GET)
    public void insertOperationLogOfParty(@RequestParam String cmd,@RequestParam String partyId,String addressId, @RequestParam String adminUserId ,@RequestParam Map<String,String> contentMap) {

        Party party =  partyService.findById(partyId);
        String partyName = "";
        if(party!=null){
            partyName = party.getName();
        }
        String addressName =danmuAddressService.findById(addressId).getName();
        String userName = adminUserService.findById(adminUserId).getUserName();
        contentMap.put("partyName",partyName);
        contentMap.put("address",addressName);
        contentMap.put("adminUser",userName);
        if ("A_Activity".equals(cmd)) {
            log.info("记录日志信息【活动】");
            operationRpcLogService.insertOperationLog("A_Activity", contentMap, adminUserId);//【活动】
        } else if ("A_Moive".equals(cmd)) {
            log.info("记录日志信息【电影】");
            operationRpcLogService.insertOperationLog("A_Moive", contentMap, adminUserId);//【电影】
        } else if ("A_density".equals(cmd)) {
            log.info("记录日志信息弹幕密度");
            operationRpcLogService.insertOperationLog("A_density", contentMap, adminUserId);//弹幕密度
        } else if ("A_delayTime".equals(cmd)) {
            log.info("记录日志信息延迟时间");
            operationRpcLogService.insertOperationLog("A_delayTime", contentMap, adminUserId);//延迟时间
        } else if ("A_testDanmuPlay".equals(cmd)) {
            log.info("记录日志信息测试弹幕开启");
            operationRpcLogService.insertOperationLog("A_testDanmuPlay", contentMap, adminUserId);//测试弹幕开启
        } else if ("A_testDanmuStop".equals(cmd)) {
            log.info("记录日志信息测试弹幕关闭");
            operationRpcLogService.insertOperationLog("A_testDanmuStop", contentMap, adminUserId);//测试弹幕关闭
        } else if ("A_preDanmuPlay".equals(cmd)) {
            log.info("记录日志信息预制弹幕开启");
            operationRpcLogService.insertOperationLog("A_preDanmuPlay", contentMap, adminUserId);//预制弹幕开启
        } else if ("A_preDanmuStop".equals(cmd)) {
            log.info("记录日志信息预制弹幕关闭");
            operationRpcLogService.insertOperationLog("A_preDanmuStop", contentMap, adminUserId);//预制弹幕关闭
        } else if ("A_end".equals(cmd)) {
            log.info("记录日志信息【结束】");
            operationRpcLogService.insertOperationLog("A_end", contentMap, adminUserId);//【结束】
        } else if ("A_advanceDanmu".equals(cmd)) {
            log.info("记录日志信息高级弹幕");
            operationRpcLogService.insertOperationLog("A_advanceDanmu", contentMap, adminUserId);//高级弹幕
        }else if ("A_video".equals(cmd)) {
            log.info("记录日志信息视频");
            operationRpcLogService.insertOperationLog("A_video", contentMap, adminUserId);//高级弹幕
        }
    }

    @RequestMapping(value = "/insertOperationLog", method = RequestMethod.GET)
    public void insertOperationLog(@RequestParam String cmd,@RequestParam String partyId,String addressId, @RequestParam String adminUserId) {
        //operationRpcLogService.insertOperationLog(key,content,adminUserId);
        Map<String, String> contentMap = new HashMap<>();

        Party party =  partyService.findById(partyId);
        String partyName = "";
        if(party!=null){
             partyName = party.getName();
        }
        String addressName =danmuAddressService.findById(addressId).getName();
        String userName = adminUserService.findById(adminUserId).getUserName();
        contentMap.put("partyName",partyName);
        contentMap.put("address",addressName);
        contentMap.put("adminUser",userName);
        if ("projectorStart".equals(cmd)) {
            log.info("记录日志信息【投影开启】");
            operationRpcLogService.insertOperationLog("M_projectorPlay", contentMap, adminUserId);//【投影开启】
        } else if ("projectorClose".equals(cmd)) {
            log.info("记录日志信息【投影关闭】");
            operationRpcLogService.insertOperationLog("M_projectorStop", contentMap, adminUserId);//【投影关闭】
        } else if ("projectorChange".equals(cmd)) {
            log.info("记录日志信息【投影切白】");
            operationRpcLogService.insertOperationLog("M_projectorShift", contentMap, adminUserId);//【投影切白】
        } else if ("appRestart".equals(cmd)) {
            log.info("记录日志信息app【重启】");
            operationRpcLogService.insertOperationLog("M_appReboost", contentMap, adminUserId);//【投影重启】
        } else if ("appStart".equals(cmd)) {
            log.info("记录日志信息app【开启】");
            operationRpcLogService.insertOperationLog("M_appPlay", contentMap, adminUserId);//app【开启】
        } else if ("appClose".equals(cmd)) {
            log.info("记录日志信息app【关闭】");
            operationRpcLogService.insertOperationLog("M_appEnd", contentMap, adminUserId);//app【关闭】
        } else if ("videoDown".equals(cmd)) {
            log.info("记录日志信息【特效视频下载】");
            operationRpcLogService.insertOperationLog("M_videoDownload", contentMap, adminUserId);//【特效视频下载】
        } else if ("expressionDown".equals(cmd)) {
            log.info("记录日志信息【表情下载】");
            operationRpcLogService.insertOperationLog("M_expressionDownload", contentMap, adminUserId);//【表情下载】
        } else if ("expressionDown".equals(cmd)) {
            log.info("记录日志信息【特效图片下载】");
            operationRpcLogService.insertOperationLog("M_specialPicDownload", contentMap, adminUserId);//【特效图片下载】
        } else if ("timerDmDown".equals(cmd)) {
            log.info("记录日志信息【定时弹幕下载】");
            operationRpcLogService.insertOperationLog("M_timerDanmuDownload", contentMap, adminUserId);//【定时弹幕下载】
        } else if ("adDmDown".equals(cmd)) {
            log.info("记录日志信息【广告弹幕下载】");
            operationRpcLogService.insertOperationLog("M_adDanmuDownload", contentMap, adminUserId);//【广告弹幕下载】
        } else if ("resourceAllDown".equals(cmd)) {
            log.info("记录日志信息【下载所有资源】");
            operationRpcLogService.insertOperationLog("M_allRssDownload", contentMap, adminUserId);//【下载所有资源】
        } else if ("configCreate".equals(cmd)) {
            log.info("记录日志信息【生成配置表】");
            operationRpcLogService.insertOperationLog("M_configGenerate", contentMap, adminUserId);//【生成配置表】
        } else if ("".equals(cmd)) {
            log.info("记录日志信息【重新加载预置弹幕】");
            operationRpcLogService.insertOperationLog("M_reLoadPreDanmu", contentMap, adminUserId);//【重新加载预置弹幕】
        } else if ("danmu-start".equals(cmd)) {
            log.info("记录日志信息模拟指令【确定】");
            operationRpcLogService.insertOperationLog("M_cmdConfirm", contentMap, adminUserId);//模拟指令【确定】
        } else if ("movie-start".equals(cmd)) {
            log.info("记录日志信息模拟指令【电影开始】");
            operationRpcLogService.insertOperationLog("M_cmdMovieStart", contentMap, adminUserId);//模拟指令【电影开始】
        } else if ("movie-close".equals(cmd)) {
            log.info("记录日志信息模拟指令【电影结束】");
            operationRpcLogService.insertOperationLog("M_cmdMovieEnd", contentMap, adminUserId);//模拟指令【电影结束】
        } else if ("updateClientDown".equals(cmd)) {
            log.info("记录日志信息【客户端下载（先）】");
            operationRpcLogService.insertOperationLog("M_clientDownload", contentMap, adminUserId);//【客户端下载（先）】
        } else if ("updatePlanCreate".equals(cmd)) {
            log.info("记录日志信息【更新计划（后）】");
            operationRpcLogService.insertOperationLog("M_uploadPlan", contentMap, adminUserId);//【更新计划（后）】
        } else if ("flashUpdate".equals(cmd)) {
            log.info("记录日志信息【flash升级】");
            operationRpcLogService.insertOperationLog("M_flashUpgrade", contentMap, adminUserId);//【flash升级】
        } else if ("flashRollBack".equals(cmd)) {
            log.info("记录日志信息【flash还原】");
            operationRpcLogService.insertOperationLog("M_flashReturn", contentMap, adminUserId);//【flash还原】
        } else if ("scriptCreate".equals(cmd)) {
            log.info("记录日志信息【生成脚本】");
            operationRpcLogService.insertOperationLog("M_scriptGenerate", contentMap, adminUserId);//【生成脚本】
        } else if ("teamViewStart1".equals(cmd)) {
            log.info("记录日志信息【开启左侧】");
            operationRpcLogService.insertOperationLog("M_leftTVPlay", contentMap, adminUserId);//【开启左侧】
        } else if ("screenPic1".equals(cmd)) {
            log.info("记录日志信息【左侧截图】");
            operationRpcLogService.insertOperationLog("M_leftTVShoot", contentMap, adminUserId);//【左侧截图】
        } else if ("".equals(cmd)) {
            log.info("记录日志信息【查看左侧截图】");
            operationRpcLogService.insertOperationLog("M_leftTVCheck", contentMap, adminUserId);//【查看左侧截图】
        } else if ("teamViewClose1".equals(cmd)) {
            log.info("记录日志信息【关闭左侧】");
            operationRpcLogService.insertOperationLog("M_leftTVEnd", contentMap, adminUserId);//【关闭左侧】
        } else if ("teamViewStart2".equals(cmd)) {
            log.info("记录日志信息【开启右侧】");
            operationRpcLogService.insertOperationLog("M_rightTVPlay", contentMap, adminUserId);//【开启右侧】
        } else if ("screenPic2".equals(cmd)) {
            log.info("记录日志信息【右侧截图】");
            operationRpcLogService.insertOperationLog("M_rightTVShoot", contentMap, adminUserId);//【右侧截图】
        } else if ("".equals(cmd)) {
            log.info("记录日志信息【查看右侧截图】");
            operationRpcLogService.insertOperationLog("M_rigthTVCheck", contentMap, adminUserId);//【查看右侧截图】
        } else if ("teamViewClose2".equals(cmd)) {
            log.info("记录日志信息【关闭右侧】");
            operationRpcLogService.insertOperationLog("M_rightTVEnd", contentMap, adminUserId);//【关闭右侧】
        } else if ("teamViewStart3".equals(cmd)) {
            log.info("记录日志信息【开启第三台】");
            operationRpcLogService.insertOperationLog("M_thridTVPlay", contentMap, adminUserId);//【开启第三台】
        } else if ("screenPic3".equals(cmd)) {
            log.info("记录日志信息【第三台截图】");
            operationRpcLogService.insertOperationLog("M_thridTVShoot", contentMap, adminUserId);//【第三台截图】
        } else if ("".equals(cmd)) {
            log.info("记录日志信息【查看第三台】");
            operationRpcLogService.insertOperationLog("M_thridTVCheck", contentMap, adminUserId);//【查看第三台】
        } else if ("teamViewClose3".equals(cmd)) {
            log.info("记录日志信息【关闭第三台】");
            operationRpcLogService.insertOperationLog("M_thirdTVEnd", contentMap, adminUserId);//【关闭第三台】
        } else if ("projectorNewStart".equals(cmd)) {
            log.info("记录日志信息【投影开启（杭州厅）】");
            operationRpcLogService.insertOperationLog("M_projectorPlay(Hangzhou)", contentMap, adminUserId);//【投影开启（杭州厅）】
        }
    }

}

