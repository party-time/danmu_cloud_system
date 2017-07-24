package cn.partytime.rpc;

import cn.partytime.model.manager.AdminUser;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.monitor.Monitor;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechatMsgTmpl.MsgTmpl;
import cn.partytime.model.wechatMsgTmpl.ValueTmpl;
import cn.partytime.service.AdminUserService;
import cn.partytime.service.BmsWechatUserService;
import cn.partytime.service.MonitorService;
import cn.partytime.service.wechat.WechatUserService;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by administrator on 2017/7/24.
 */
public class WechatMessageRpcService {

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private BmsWechatUserService bmsWechatUserService;


    @Value("${monitor.id}")
    private String templateId;

    @Autowired
    private Configuration configuration;


    public void send(String key,Map<String,String> content){
        Monitor monitor = monitorService.findByKey(key);
        if( null == monitor ){
            throw new IllegalArgumentException("报警的key不存在");
        }
        String patternString = "\\$\\{(" + StringUtils.join(content.keySet(), "|") + ")\\}";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(monitor.getContent());
        //两个方法：appendReplacement, appendTail
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb, content.get(matcher.group(1)));
        }
        matcher.appendTail(sb);

        String msg = sb.toString();

        if( null == monitor.getAdminUserIds() || "".equals(monitor.getAdminUserIds())){
            throw new IllegalArgumentException("报警的管理员不存在");
        }
        if(monitor.getAdminUserIds().indexOf(",") != -1){
            String[] ids = monitor.getAdminUserIds().split(",");
            for(String id : ids){
                AdminUser adminUser =  adminUserService.findById(id);
                WechatUser wechatUser = wechatUserService.findById(adminUser.getWechatId());
                sendTmpl(wechatUser.getOpenId(),monitor.get,msg);
            }


        }else{
            AdminUser adminUser = adminUserService.findById(monitor.getAdminUserIds());
            WechatUser wechatUser = wechatUserService.findById(adminUser.getWechatId());
            sendTmpl(wechatUser.getOpenId(),,msg);
        }
    }

    private void sendTmpl(String openId,String tempId, String msg){
        MsgTmpl msgTmpl = new MsgTmpl();
        msgTmpl.setTouser(openId);

        msgTmpl.setTemplate_id(tempId);
        msgTmpl.setTopcolor("#FF0000");
        msgTmpl.setUrl("");

        Map<String,ValueTmpl> content = new HashMap<String,ValueTmpl>();
        ValueTmpl valueTmpl = new ValueTmpl();
        valueTmpl.setValue("有人购买商品");
        content.put("first",valueTmpl);

        ValueTmpl valueTmpl1 = new ValueTmpl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        valueTmpl1.setValue(sdf.format(new Date()));
        content.put("keyword1",valueTmpl1);

        ValueTmpl valueTmpl2 = new ValueTmpl();
        valueTmpl2.setValue("电商系统");
        content.put("keyword2",valueTmpl2);

        ValueTmpl valueTmpl5 = new ValueTmpl();
        DanmuAddress danmuAddress = danmuAddressService.findById(order.getAddressId());
        if( null != danmuAddress){
            valueTmpl5.setValue("有人在"+danmuAddress.getName()+"购买了"+item.getName()+"*"+order.getNum()+"，请与星美生活工作人员联系");
        }else{
            valueTmpl5.setValue("有人购买了"+item.getName()+"*"+order.getNum()+"，请与星美生活工作人员联系");
        }
        content.put("remark",valueTmpl5);
        msgTmpl.setData(content);

        bmsWechatUserService.sendMsg(msgTmpl);
    }

}
