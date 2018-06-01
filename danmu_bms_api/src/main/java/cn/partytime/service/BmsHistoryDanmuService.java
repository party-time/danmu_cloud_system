package cn.partytime.service;

import cn.partytime.common.util.ComponentKeyConst;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcCmdService;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.CmdTempComponentData;
import cn.partytime.model.DanmuLogicModel;
import cn.partytime.model.danmu.Danmu;
import cn.partytime.model.danmu.DanmuPool;
import cn.partytime.model.user.UserPrize;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.danmuCmd.BmsCmdService;
import cn.partytime.service.wechat.WechatUserService;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by lENOVO on 2016/12/7.
 */

@Service
public class BmsHistoryDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(BmsHistoryDanmuService.class);

    @Autowired
    private DanmuService danmuService;

    @Autowired
    private UserPrizeService userPrizeService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private DanmuPoolService danmuPoolService;

    @Autowired
    private RedisService redisService;

    @Value("${historydanmu.tempPath}")
    private String tempPath;

    @Autowired
    private BmsCmdService bmsCmdService;


    @Autowired
    private DanmuCommonService danmuCommonService;


    @Autowired
    private RpcCmdService rpcCmdService;

    public  List<Map<String,Object>> findHistoryDanmu(String partyId, int time, int count) {
        List<DanmuPool> danmuPoolList = danmuPoolService.findByPartyId(partyId);

        List<Map<String,Object>> danmuList = new ArrayList<Map<String,Object>>();
        if (ListUtils.checkListIsNotNull(danmuPoolList)) {
            List<String> poolIdList = new ArrayList<String>();
            danmuPoolList.forEach(e -> poolIdList.add(e.getId()));
            List<Danmu> danmuModelList = danmuService.findDanmuListByPartyIdAndTimeAndDanmuPool(partyId, time, poolIdList, count);
            if (ListUtils.checkListIsNotNull(danmuModelList)) {
                for (Danmu danmuModel : danmuModelList) {

                    Map<String,Object> timerDanmuMap = new HashMap<String,Object>();
                    String templateId = danmuModel.getTemplateId();

                    if(!"0".equals(templateId)){
                        Map<String,Object> contentMap = danmuModel.getContent();
                        CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByIdFromCache(templateId);
                        List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
                        if(ListUtils.checkListIsNotNull(cmdTempComponentDataList)){
                            for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                                String key = cmdTempComponentData.getKey();
                                if(!contentMap.containsKey(key)){
                                    int type = cmdTempComponentData.getType();
                                    if(type==3){
                                        List<Object> list = new ArrayList<Object>();
                                        list.add(cmdTempComponentData.getDefaultValue());

                                        contentMap.put(key,list);
                                    }else{
                                        contentMap.put(key,cmdTempComponentData.getDefaultValue());
                                    }
                                }
                            }
                            timerDanmuMap.put("data",contentMap);
                            timerDanmuMap.put("type",cmdTempAllData.getKey());
                        }
                    }else{
                        timerDanmuMap.put("data",danmuModel.getContent());
                        timerDanmuMap.put("type","vedio");
                    }
                    timerDanmuMap.put("beginTime",danmuModel.getTime());
                    danmuList.add(timerDanmuMap);
                }
            }
        }
        return danmuList;
    }


    public String historyDanmuExport(String partyId,String addressIdArray) {

        logger.info("历史弹幕导出...........");
        String[] addressArray = {};
        List<String> addressList = new ArrayList<>();
        if(!StringUtils.isEmpty(addressIdArray)){
            addressArray = addressIdArray.split(",");
            for(String addressId:addressArray){
                addressList.add(addressId);
            }
        }

        //DanmuPool danmuPool = danmuPoolService.findDanmuPoolbyPartyIdAndAddressId(addressId, partyId);

        List<DanmuPool> danmuPoolList = null;
        if(ListUtils.checkListIsNotNull(addressList)){
            logger.info("历史弹幕（addressList）数量:{}",addressList.size());
            danmuPoolList = danmuPoolService.findByPartyIdAndAddressList(partyId,addressList);
        }else{
            logger.info("历史弹幕（addressList）数量:{}",0);
            danmuPoolList = danmuPoolService.findByPartyId(partyId);
        }



        List<DanmuLogicModel> danmuLogicModelList = null;
        UUID uuid = UUID.randomUUID();
        String fileName = tempPath + File.separator + uuid + ".xls";

        logger.info("filePath:{}",fileName);
        if (ListUtils.checkListIsNotNull(danmuPoolList)) {
            //String poolId = danmuPool.getId();
            List<String> danmuPoolIdList = new ArrayList<>();
            for(DanmuPool danmuPool:danmuPoolList){
                danmuPoolIdList.add(danmuPool.getId());
            }

            long count = danmuService.countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlg(danmuPoolIdList, 1, false, true);
            int page = 0;
            int size = 300;
            logger.info("弹幕总数:{}",count);
            if (count > 0) {
                danmuLogicModelList = new ArrayList<DanmuLogicModel>();
                while (page * size < count) {
                    danmuLogicModelList.addAll(findDanmuLogicModelList(danmuPoolIdList, page, size));
                    page = page + 1;
                }
                createFile(danmuLogicModelList, partyId, fileName);
                return fileName;
            }
        }
        return "";
    }

    private List<DanmuLogicModel> findDanmuLogicModelList(List<String>danmuPoolIdLIST, Integer page, Integer size) {
        List<String> userIdList = new ArrayList<String>();
        List<String> danmuIdList = new ArrayList<String>();

        List<DanmuLogicModel> danmuLogicModelList = new ArrayList<DanmuLogicModel>();
        //Page<Danmu> danmuListByPage = danmuService.findByDanmuPoolIdAndDanmuSrcAndIsBlockedAndViewFlg(poolId, 1, false, true, page, size);
        Page<Danmu> danmuListByPage = danmuService.findByDanmuSrcAndIsBlockedAndViewFlgAndDanmuPoolIdWithin(1,false,true,page,size,danmuPoolIdLIST);
        long count = danmuListByPage.getTotalElements();
        if (count == 0) {
            return null;
        }

        List<Danmu> danmuList = danmuListByPage.getContent();
        for (Danmu danmuModel : danmuList) {
            userIdList.add(danmuModel.getCreateUserId());
            DanmuLogicModel danmuLogicModel = new DanmuLogicModel();

            String templateKey = danmuModel.getTemplateIdKey();
            CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByKeyFromCache(templateKey);
            danmuLogicModel.setKey(templateKey);
            for(CmdTempComponentData cmdTempComponentData:cmdTempAllData.getCmdTempComponentDataList()){
                Object objectMsg = null;
                String key = cmdTempComponentData.getKey();
                String value = danmuModel.getContent().get(key)+"";
                logger.info("key:"+key+"value:"+value);
                if(!"color".equals(key) && cmdTempComponentData.getType()!=1 && cmdTempComponentData.getType()!=0 ){
                    if(cmdTempComponentData.getType()==3){
                        //显示的内容
                        objectMsg = danmuCommonService.setShowArrayContent(cmdTempComponentData.getComponentType(),value,cmdTempComponentData.getComponentId(),cmdTempComponentData.getDefaultValue());
                    }else {
                        objectMsg = danmuCommonService.setShowNotArrayContent(value,cmdTempComponentData.getComponentId(),cmdTempComponentData.getType());
                    }
                    danmuLogicModel.setMsg(objectMsg);
                }
            }

            danmuIdList.add(danmuModel.getId());
            BeanUtils.copyProperties(danmuModel, danmuLogicModel);
            danmuLogicModelList.add(danmuLogicModel);
        }

        //查询弹幕获奖信息
        Map<String, UserPrize> userPrizeMap = new HashMap<String, UserPrize>();
        List<UserPrize> userPrizeList = userPrizeService.findUserPrizeByDanmuList(danmuIdList);
        if (ListUtils.checkListIsNotNull(userPrizeList)) {
            for (UserPrize userPrize : userPrizeList) {
                userPrizeMap.put(userPrize.getDanmuId(), userPrize);
            }
        }
        //查询微信用户列表
        List<WechatUser> wechatUserList = wechatUserService.findWechatUserByUserIdList(userIdList);
        Map<String, WechatUser> wechatUserMap = new HashMap<String, WechatUser>();
        if (ListUtils.checkListIsNotNull(wechatUserList)) {
            for (WechatUser wechatUser : wechatUserList) {
                wechatUserMap.put(wechatUser.getUserId(), wechatUser);
            }
        }


        for (DanmuLogicModel danmuLogicModel : danmuLogicModelList) {
            WechatUser wechatUser = wechatUserMap.get(danmuLogicModel.getCreateUserId());
            if (wechatUser != null) {
                danmuLogicModel.setUrl(wechatUser.getImgUrl());
                danmuLogicModel.setOpenId(wechatUser.getOpenId());
                danmuLogicModel.setNick(wechatUser.getNick());
            }
            danmuLogicModel.setSend(userPrizeMap.get(danmuLogicModel.getId()) == null ? false : true);
        }
        return danmuLogicModelList;
    }


    private void createFile(List<DanmuLogicModel> danmuLogicModelList, String partyId, String fileName) {

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("弹幕");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("弹幕");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("时间");
        cell.setCellStyle(style);
        FileOutputStream fileOutputStream = null;
        try {
            if (ListUtils.checkListIsNotNull(danmuLogicModelList)) {
                for (int i = 0; i < danmuLogicModelList.size(); i++) {
                    DanmuLogicModel danmuLogicModel = danmuLogicModelList.get(i);
                    row = sheet.createRow((int) i + 1);
                    // 第四步，创建单元格，并设置值
                    row.createCell((short) 0).setCellValue(danmuLogicModel.getNick() == null ? "未知" : danmuLogicModel.getNick());
                    row.createCell((short) 1).setCellValue(danmuLogicModel.getMsg()+"");
                    row.createCell((short) 2).setCellValue(danmuLogicModel.getTime() / 60 + "分" + danmuLogicModel.getTime() % 60 + "秒");
                }
                fileOutputStream = new FileOutputStream(fileName);
                wb.write(fileOutputStream);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
