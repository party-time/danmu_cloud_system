package cn.partytime.service.shop;

import cn.partytime.logic.danmu.PageResultModel;
import cn.partytime.logic.danmu.PartyLogicModel;
import cn.partytime.logicService.PartyLogicService;
import cn.partytime.model.OrderItem;
import cn.partytime.model.manager.AdminUser;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.monitor.Monitor;
import cn.partytime.model.shop.Item;
import cn.partytime.model.shop.Order;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.model.wechatMsgTmpl.MsgTmpl;
import cn.partytime.model.wechatMsgTmpl.ValueTmpl;
import cn.partytime.service.AdminUserService;
import cn.partytime.service.BmsWechatUserService;
import cn.partytime.service.DanmuAddressService;
import cn.partytime.service.MonitorService;
import cn.partytime.service.wechat.WechatUserInfoService;
import cn.partytime.service.wechat.WechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by administrator on 2017/7/7.
 */
@Slf4j
@Service
public class BmsOrderService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private WechatUserService wechatUserService;

    @Value("${buyItemSuccess.id}")
    private String buyItemSuccess;

    @Autowired
    private BmsWechatUserService bmsWechatUserService;

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private PartyLogicService partyLogicService;


    public PageResultModel findAll(Integer pageNum, Integer size){
        Page<Order> orderPage =  orderService.findAll(pageNum,size);
        if( null != orderPage){
            PageResultModel pageResultModel = new PageResultModel();
            List<OrderItem> orderItemList = new ArrayList<>();
            for(Order order : orderPage.getContent()){
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                if(!StringUtils.isEmpty(order.getItemId())){
                    Item item = itemService.findById(order.getItemId());
                    orderItem.setItem(item);
                }
                if(!StringUtils.isEmpty(order.getAdminId())){
                    AdminUser adminUser = adminUserService.findById(order.getAdminId());
                    orderItem.setAdminUser(adminUser);
                }
                if(!StringUtils.isEmpty(order.getOpenId())){
                    WechatUser wechatUser = wechatUserService.findByOpenId(order.getOpenId());
                    orderItem.setWechatUser(wechatUser);
                }
                orderItemList.add(orderItem);
            }
            pageResultModel.setRows(orderItemList);
            pageResultModel.setTotal(orderPage.getTotalElements());
            return pageResultModel;
        }else{
            return null;
        }
    }

    public void sendBuySuccess(String openId,String orderId){
        MsgTmpl msgTmpl = new MsgTmpl();
        msgTmpl.setTouser(openId);
        msgTmpl.setTemplate_id(buyItemSuccess);
        msgTmpl.setTopcolor("#FF0000");
        msgTmpl.setUrl("");
        Order order = orderService.findById(orderId);
        Item item = itemService.findById(order.getItemId());
        Map<String,ValueTmpl> content = new HashMap<String,ValueTmpl>();
        ValueTmpl valueTmpl = new ValueTmpl();
        valueTmpl.setValue("购买成功");
        content.put("first",valueTmpl);
        ValueTmpl valueTmpl1 = new ValueTmpl();
        valueTmpl1.setValue("购买了"+item.getName()+"*"+order.getNum());
        content.put("keyword1",valueTmpl1);

        ValueTmpl valueTmpl2 = new ValueTmpl();
        valueTmpl2.setValue(order.getCode());
        content.put("keyword2",valueTmpl2);

        ValueTmpl valueTmpl3 = new ValueTmpl();
        valueTmpl3.setValue(order.getTotal_fee()/100+"元");
        content.put("keyword3",valueTmpl3);

        ValueTmpl valueTmpl4 = new ValueTmpl();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        valueTmpl4.setValue(sdf.format(cal.getTime()));
        content.put("keyword4",valueTmpl4);

        ValueTmpl valueTmpl5 = new ValueTmpl();
        valueTmpl5.setValue("请在有效期之前，在星美生活领取您的商品,向工作人员出示您的兑换码，如有任何问题，请联系13888888888");
        content.put("remark",valueTmpl5);
        msgTmpl.setData(content);
        bmsWechatUserService.sendMsg(msgTmpl);
    }

    public void sendAdminOrder(String key,String orderId){
        Monitor monitor = monitorService.findByKey(key);
        Order order = orderService.findById(orderId);
        Item item = itemService.findById(order.getItemId());
        String adminUserIdStr = monitor.getAdminUserIds();

        List<String> adminUserIdList = null;
        if(adminUserIdStr.indexOf(",") != -1){
            String[] adminUserIds = adminUserIdStr.split(",");
            adminUserIdList = Arrays.asList(adminUserIds);
        }else{
            adminUserIdList = new ArrayList<>();
            adminUserIdList.add(adminUserIdStr);
        }

        for(String adminUserId : adminUserIdList){
            WechatUser wechatUser = wechatUserService.findById(adminUserId);
            MsgTmpl msgTmpl = new MsgTmpl();
            msgTmpl.setTouser(wechatUser.getOpenId());

            msgTmpl.setTemplate_id(monitor.getWechatTempId());
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

    public String adminOperational(String content,String wechatId) {
        if (content.indexOf("兑换") != -1) {
            AdminUser adminUser = adminUserService.findByWechatId(wechatId);
            if (null != adminUser) {
                String code = content.substring(content.indexOf("兑换")+2);
                log.info("code:"+code);
                Order order = orderService.findByCodeAndGetItemTime(code, null);
                if (null == order) {
                    return "不存在此兑换码或者兑换码已经被使用，请确认后重新输入";
                }
                orderService.updateGetItem(code, adminUser.getId());
                Item item = itemService.findById(order.getItemId());
                return "兑换成功，商品名称:"+item.getName()+"数量:"+order.getNum();
            }
        } else if (content.indexOf("开店") != -1) {
            AdminUser adminUser = adminUserService.findByWechatId(wechatId);
            if( null != adminUser){
                WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatId);
                if( null != wechatUserInfo){
                    PartyLogicModel partyLogicModel =partyLogicService.findPartyByLonLat(wechatUserInfo.getLastLongitude(), wechatUserInfo.getLastLatitude());
                    if( null != partyLogicModel){
                        danmuAddressService.updateShopStatus(partyLogicModel.getAddressId(), 0);
                        return "开店成功";
                    }else{
                        return "开店失败，不存在该电影院";
                    }
                }
            }
        } else if (content.indexOf("闭店") != -1) {
            AdminUser adminUser = adminUserService.findByWechatId(wechatId);
            if( null != adminUser){
                WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatId);
                if( null != wechatUserInfo){
                    PartyLogicModel partyLogicModel =partyLogicService.findPartyByLonLat(wechatUserInfo.getLastLongitude(), wechatUserInfo.getLastLatitude());
                    if( null != partyLogicModel){
                        danmuAddressService.updateShopStatus(partyLogicModel.getAddressId(), 1);
                        return "闭店成功";
                    }else{
                        return "闭店失败，不存在该电影院";
                    }
                }
            }
        }
        return null;

    }

}
