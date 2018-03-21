package cn.partytime.service;

import cn.partytime.common.cachekey.client.ClientCacheKey;
import cn.partytime.common.cachekey.DanmuCacheKey;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.common.util.ComponentKeyConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.config.CacheDataRepository;
import cn.partytime.dataRpc.RpcCmdService;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcOperationRpcLogService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.*;
import cn.partytime.model.danmu.Danmu;
import cn.partytime.model.danmu.DanmuLog;
import cn.partytime.model.danmu.DanmuPool;
import cn.partytime.model.manager.danmuCmdJson.CmdTemp;
import cn.partytime.model.user.UserPrize;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.danmu.DanmuLogService;
import cn.partytime.service.danmuCmdJson.CmdTempService;
import cn.partytime.service.wechat.WechatUserService;
import cn.partytime.util.CommonUtils;
import cn.partytime.util.DanmuCheckUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lENOVO on 2016/8/24.
 */

@Service
public class BmsDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(BmsDanmuService.class);
    @Autowired
    private CacheDataRepository cacheDataRepository;

    @Autowired
    private RedisService redisService;


    @Autowired
    private DanmuPoolService danmuPoolService;

    @Autowired
    private DanmuService danmuService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private AliyunGreenService aliyunGreenService;

    @Autowired
    private UserPrizeService userPrizeService;


    @Autowired
    private BmsColorService bmsColorService;



    @Autowired
    private DanmuLogService danmuLogService;



    @Autowired
    private CmdTempService cmdTempService;


    @Autowired
    private DanmuCommonService danmuCommonService;

    @Autowired
    private RpcCmdService rpcCmdService;

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private RpcOperationRpcLogService rpcOperationRpcLogService;

    /**
     * ‘
     * 判断用户是不是频繁登录
     *
     * @return
     */
    public boolean checkFrequency(HttpServletRequest request) {
        String key = "danmuNew-last-send-time-user-" + CommonUtils.getIp(request);
        //ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        boolean has = !StringUtils.isEmpty(redisService.get(key));
        redisService.set(key, "1");
        redisService.expire(key, 2);
        return has;
    }

    public boolean checkDanmuIsRepeat(String openId,String message){
        String key = ClientCacheKey.CLIENT_DANMU_CHACHE+ CommonConst.COLON+openId+CommonConst.COLON+message;
        boolean has = !StringUtils.isEmpty(redisService.get(key));
        if(!has){
            redisService.set(key, "1");
            redisService.expire(key, 10);
        }
        return has;
    }



    /**
     * 检验弹幕是否合法
     *
     * @param msg
     * @return
     */
    public boolean check(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return false;
        }
        return cacheDataRepository.matchBlockKey(replaceBlank(msg).toLowerCase());
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 验证弹幕是否合法
     *
     * @param msg
     * @return
     */
    public RestResultModel checkDanmuIsOk(String msg) {
        RestResultModel restResultModel = new RestResultModel();
        if (check(msg)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("msg is blocked");
            logger.info("消息含有屏蔽词");
            return restResultModel;
        } else if (aliyunGreenService.blockTextKeyword(msg)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("msg is green blocked");
            logger.info("消息被阿里云屏蔽");
            return restResultModel;
        } else if (DanmuCheckUtil.checkDanmuIsAllBlack(msg)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("msg format is wrong");
            logger.info("消息格式错误");
            return restResultModel;
        }
        return null;
    }



    public List<DanmuLogicModel> findDanmuListByIdList(List<String> idList){

        if(ListUtils.checkListIsNull(idList)){
            return null;
        }
        return  findDanmuLogicModelList(danmuService.findDanmuByIdList(idList));

    }



    public PageResultModel findPageResultModel(int index, int size, String addressIdArray, String partyId, int danmuSrc) {

        String[] addressArray = {};
        List<String> addressList = new ArrayList<String>();
        if(!StringUtils.isEmpty(addressIdArray)){
            addressArray = addressIdArray.split(",");
            for(String addressId:addressArray){
                addressList.add(addressId);
            }
        }

        PageResultModel pageResultModel = new PageResultModel();

        Map<String, Object> result = new HashMap<String, Object>();
        //DanmuPool danmuPool = findDanmuPool(addressId, partyId);
        //String danmuPoolId = danmuPool.getId();

        List<DanmuPool> danmuPoolList = null;
        if(ListUtils.checkListIsNotNull(addressList)){
            danmuPoolList = danmuPoolService.findByPartyIdAndAddressList(partyId,addressList);
        }else{
            danmuPoolList = danmuPoolService.findByPartyId(partyId);
        }
        List<DanmuLogicModel> danmuLogicModelList = new ArrayList<DanmuLogicModel>();
        logger.info("当前活动下绑定的弹幕池信息:{}",JSON.toJSONString(danmuPoolList));
        List<String> danmuPoolIdLIST = new ArrayList<String>();
        if(ListUtils.checkListIsNotNull(danmuPoolList)){
            for(DanmuPool danmuPool:danmuPoolList){
                danmuPoolIdLIST.add(danmuPool.getId());
            }
            logger.info("当前活动下绑定的弹幕池数量:{}",danmuPoolIdLIST.size());
        }else{
            pageResultModel.setRows(danmuLogicModelList);
            return pageResultModel;
        }


        Page<Danmu> danmuListByPage = danmuService.findByDanmuSrcAndIsBlockedAndViewFlgAndDanmuPoolIdWithin(1,false,true,index,size,danmuPoolIdLIST);
        long count = danmuListByPage.getTotalElements();
        pageResultModel.setTotal(count);
        if(count==0){
            pageResultModel.setRows(danmuLogicModelList);
            return pageResultModel;
        }
        List<Danmu> timerDanmuList = danmuListByPage.getContent();

        pageResultModel.setRows(findDanmuLogicModelList(timerDanmuList));
        return pageResultModel;
    }


    public List<DanmuLogicModel> findDanmuLogicModelList(List<Danmu> danmuIdList){
        List<DanmuLogicModel> danmuLogicModelList = new ArrayList<DanmuLogicModel>();
        List<String> userIdList = new ArrayList<String>();
        List<String> IdList = new ArrayList<String>();
        CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByIdFromCache(ComponentKeyConst.P_DANMU);


        for (Danmu danmuModel : danmuIdList) {
            userIdList.add(danmuModel.getCreateUserId());
            //DanmuLogicModel danmuLogicModel = new DanmuLogicModel();
            //BeanUtils.copyProperties(danmuModel, danmuLogicModel);
            DanmuLogicModel danmuLogicModel = new DanmuLogicModel();
            for(CmdTempComponentData cmdTempComponentData:cmdTempAllData.getCmdTempComponentDataList()){

                Object objectMsg = null;
                String value = danmuModel.getContent().get(cmdTempComponentData.getKey())+"";
                if(cmdTempComponentData.getType()==3){
                    //显示的内容
                    objectMsg = danmuCommonService.setShowArrayContent(cmdTempComponentData.getComponentType(),value,cmdTempComponentData.getComponentId(),cmdTempComponentData.getDefaultValue());
                }else{
                    objectMsg = danmuCommonService.setShowNotArrayContent(value,cmdTempComponentData.getComponentId(),cmdTempComponentData.getType());
                }
                danmuLogicModel.setMsg(objectMsg);
            }

            IdList.add(danmuModel.getId());
            BeanUtils.copyProperties(danmuModel, danmuLogicModel);
            logger.info("danmuLogicModel对象:{}",JSON.toJSONString(danmuLogicModel));
            danmuLogicModelList.add(danmuLogicModel);
        }

        //查询弹幕获奖信息
        Map<String, UserPrize> userPrizeMap = new HashMap<String, UserPrize>();
        List<UserPrize> userPrizeList = userPrizeService.findUserPrizeByDanmuList(IdList);
        if (ListUtils.checkListIsNotNull(userPrizeList)) {
            for (UserPrize userPrize : userPrizeList) {
                userPrizeMap.put(userPrize.getDanmuId(), userPrize);
            }
        }

        //查询微信用户列表
        logger.info("userIdList对象:{}",JSON.toJSONString(userIdList));
        List<WechatUser> wechatUserList = wechatUserService.findWechatUserByUserIdList(userIdList);
        Map<String, WechatUser> wechatUserMap = new HashMap<String, WechatUser>();
        for (WechatUser wechatUser : wechatUserList) {
            wechatUserMap.put(wechatUser.getUserId(), wechatUser);
        }

        for (DanmuLogicModel danmuLogicModel : danmuLogicModelList) {
            WechatUser wechatUser = wechatUserMap.get(danmuLogicModel.getCreateUserId());
            logger.info("wechatUser对象:{}",JSON.toJSONString(wechatUser));
            if(wechatUser!=null){
                danmuLogicModel.setUrl(wechatUser.getImgUrl());
                danmuLogicModel.setOpenId(wechatUser.getOpenId());
                danmuLogicModel.setNick(wechatUser.getNick());
            }
            danmuLogicModel.setSend(userPrizeMap.get(danmuLogicModel.getId()) == null ? false : true);
        }

        return danmuLogicModelList;
    }





    /**
     * 语音弹幕发送
     * @param templateId
     * @param openId
     * @param partyId
     * @param addressId
     * @param danmuSrc   弹幕来源 管理员:0,微信用户:1
     * @param danmuType 0:非语音 1：语音弹幕
     */
    /**
     * 语音弹幕发送
     * @param cmdKey
     * @param openId
     * @param partyId
     * @param addressId
     * @param danmuSrc   弹幕来源 管理员:0,微信用户:1
     * @param danmuType 0:非语音 1：语音弹幕
     */
    public void sendDanmuByWechat(String cmdKey,Map<String,String> danmuMap,String openId,String partyId,String addressId,int danmuSrc,int danmuType){

        logger.info("指令key:{},消息内容:{},openId:{},活动编号:{},地址编号:{},弹幕来源:{},弹幕类型:{}",cmdKey,JSON.toJSONString(danmuMap),openId,partyId,addressId,danmuSrc,danmuType);
        CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByKeyFromCache(cmdKey);
        String cmdName = cmdTempAllData.getName();
        String templateId = cmdTempAllData.getId();
        //是否入弹幕库 0入库  1不入库
        int isInDanmuLib = cmdTempAllData.getIsInDanmuLib()==null?1:cmdTempAllData.getIsInDanmuLib();

        if(StringUtils.isEmpty(addressId)){
            logger.info("活动编号或者场地编号是空！");
            return;
        }

        DanmuAddressModel danmuAddress = rpcDanmuAddressService.findById(addressId);
        if(danmuAddress==null){
            logger.info("没有场地!");
            return;
        }

        Date date = DateUtils.getCurrentDate();
        PartyLogicModel party = rpcPartyService.findPartyByAddressId(addressId);
        int time = 0;

        //临时
        if(danmuAddress.getType()==1){
            if(party!=null){
                logger.info("当前场地有活动正在进行");
                //return;
                time =calculateDanmuTime(party,date);
            }
        }else{
            if(party==null){
                logger.info("当前场地有活动正在进行");
                return;
            }
        }


        WechatUser wechatUser = getWechatUserInfo(openId);
        if(wechatUser==null){
            logger.info("微信用户不存在");
            return;
        }



        boolean bCheck=false;
        CmdTempComponentData checkCmdTempComponentData = null;
        Object msgObject = null;
        //模板下的所有组件
        List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
        Map<String,Object> map = new HashMap<String,Object>();
        if(ListUtils.checkListIsNotNull(cmdTempComponentDataList)){
            for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){

                String key = cmdTempComponentData.getKey();

                //数据类型
                int type = cmdTempComponentData.getType();

                //组件的id 0无组件 1特效视频 2特效图片 3表情图片
                String componentId = cmdTempComponentData.getComponentId();
                int isCheck  = cmdTempComponentData.getIsCheck();

                //组件的类型 0text 1textarea 2select  3radiobutton 4checkbox
                Integer componentType = cmdTempComponentData.getComponentType();

                Object content = null;
                Object msgContent = null;

                boolean specialCompontentBoolean =  danmuCommonService.checkSpecialComponent(componentId);
                if(specialCompontentBoolean && "0".equals(componentId)){
                    map.put(key,cmdTempComponentData.getDefaultValue());
                }else{
                    if(type==3){
                        //显示的内容
                        logger.info("current key:{}",key);
                        content = danmuCommonService.setProtocolArrayContent(componentType,danmuMap.get(key),cmdTempComponentData.getDefaultValue());
                        msgContent = danmuCommonService.setShowArrayContent(componentType,danmuMap.get(key),componentId,cmdTempComponentData.getDefaultValue());
                        if(msgContent!=null){
                            List<String> messageList = (List<String>)msgContent;
                            for(int i=0; i<messageList.size(); i++){
                                RestResultModel restResultModel = checkDanmuIsOk(messageList.get(i));
                                if(restResultModel!=null){
                                    return;
                                }
                            }

                        }
                    }else{
                        msgContent = danmuCommonService.setShowNotArrayContent(danmuMap.get(key),componentId,type);
                        content = danmuCommonService.setShowNotArrayContent(danmuMap.get(key),type);
                        RestResultModel restResultModel = checkDanmuIsOk(String.valueOf(msgContent));
                        if(restResultModel!=null){
                            return;
                        }
                    }
                }

                if(isCheck==0){
                    checkCmdTempComponentData = cmdTempComponentData;
                    bCheck = true;
                    //显示内容
                    msgObject = msgContent;
                }
                map.put(key,content);
            }
            //是否保存弹幕

            String poolId = "";
            if(party!=null){
                DanmuPool danmuPool = findDanmuPool(addressId, partyId);
                poolId = danmuPool.getId();
            }else{
                poolId = "";
            }
            //弹幕池编号
            //String poolId = danmuPool.getId();
            Danmu danmuModel = new Danmu();
            danmuModel.setTemplateId(templateId);
            danmuModel.setContent(map);
            danmuModel.setBlocked(false);
            danmuModel.setCreateTime(date);
            danmuModel.setUpdateTime(date);
            danmuModel.setCreateUserId(wechatUser.getUserId());
            danmuModel.setUpdateUserId(wechatUser.getUserId());
            danmuModel.setDanmuPoolId(poolId);
            danmuModel.setDanmuSrc(danmuSrc);
            danmuModel.setViewFlg(false);

            danmuModel.setTime(time);
            //弹幕类型
            danmuModel.setType(danmuType);

            //是否保存弹幕
            if(isInDanmuLib==0){
                danmuModel = danmuService.save(danmuModel);
            }

            //记录弹幕历史
            DanmuLog danmuLog = saveDanmuModel(danmuModel);

            if(bCheck){
                //发送弹幕到
                logger.info("发送给服务器的弹幕信息:{}",JSON.toJSONString(danmuLog));
                sendDanmuToMq(partyId, addressId,danmuLog,wechatUser,party.getType(),cmdName,checkCmdTempComponentData);

            }else{
                CmdTemp cmdTemp = cmdTempService.findById(danmuLog.getTemplateId());
                Map<String,Object> commandObject = new HashMap<String,Object>();
                commandObject.put("type",cmdTemp.getKey());
                commandObject.put("data",danmuLog.getContent());
                pubMessageCollectorServer(partyId,addressId,commandObject);
            }
        }

    }


    /**
     * 发送弹幕
     * @param request
     * @param openId
     * @param danmuType
     * @return
     */
    public RestResultModel sendDanmu(HttpServletRequest request,String openId,int danmuType){
        Integer danmuSrc = 1;
        RestResultModel restResultModel = new RestResultModel();
        String templateId = request.getParameter("templateId");//模板编号
        String partyId = request.getParameter("partyId");//活动编号
        String addressId = request.getParameter("addressId");//地址编号
        logger.info("指令编号:{},openId:{},活动编号:{},地址编号:{},弹幕来源:{},弹幕类型:{}",templateId,openId,partyId,addressId,danmuSrc,danmuType);

        CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByIdFromCache(templateId);//组件信息
        String cname = cmdTempAllData.getName();
        int isInDanmuLib = cmdTempAllData.getIsInDanmuLib()==null?1:cmdTempAllData.getIsInDanmuLib();
        Date date = DateUtils.getCurrentDate();

        PartyLogicModel party = rpcPartyService.findPartyByAddressId(addressId);
        if(party==null){
            logger.info("当前场地没有活动正在进行");
            restResultModel.setResult(404);
            restResultModel.setResult_msg("没有活动正在进行");
            return restResultModel;
        }


        WechatUser wechatUser = getWechatUserInfo(openId);
        if(wechatUser==null){
            logger.info("微信用户不存在");
            restResultModel.setResult(404);
            restResultModel.setResult_msg("微信用户不存在");
            return restResultModel;
        }

        int time = calculateDanmuTime(party,date);

        Object msg = null;
        boolean bCheck=false;

        CmdTempComponentData checkCmdTempComponentData = null;

        boolean isBlock = false;
        //模板下的所有组件
        List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
        Map<String,Object> map = new HashMap<String,Object>();
        if(ListUtils.checkListIsNotNull(cmdTempComponentDataList)){
            for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                String key = cmdTempComponentData.getKey();
                //数据类型
                int type = cmdTempComponentData.getType();
                //组件的id 0无组件 1特效视频 2特效图片 3表情图片
                String componentId = cmdTempComponentData.getComponentId();

                logger.info("当前的组件类型:{}",componentId);
                int isCheck  = cmdTempComponentData.getIsCheck();
                Integer componentType = cmdTempComponentData.getComponentType();//组件的类型 0text 1textarea 2select  3radiobutton 4checkbox
                Object content = null;
                Object msgContent = null;
                boolean specialCompontentBoolean =  danmuCommonService.checkSpecialComponent(componentId);
                if(specialCompontentBoolean && "0".equals(componentId)){
                    map.put(key,cmdTempComponentData.getDefaultValue());
                }else{
                    if(type==3){
                        //显示的内容
                        content = danmuCommonService.setProtocolArrayContent(componentType,request.getParameter(key),cmdTempComponentData.getDefaultValue());
                        msgContent = danmuCommonService.setShowArrayContent(componentType,request.getParameter(key),componentId,cmdTempComponentData.getDefaultValue());
                        if(msgContent!=null && (!"1".equals(componentId) && !"2".equals(componentId))){
                            List<String> messageList = (List<String>)msgContent;
                            for(int i=0; i<messageList.size(); i++){
                                restResultModel = checkDanmuIsOk(messageList.get(i));
                                if(restResultModel!=null){
                                    isBlock=true;
                                }
                            }

                        }
                    }else{
                        if("color".equals(key)){
                            String color = request.getParameter(key);
                            if(StringUtils.isEmpty(color)){
                                content = bmsColorService.getRandomColor();
                            }else{
                                content = danmuCommonService.setShowNotArrayContent(request.getParameter(key),type);
                            }
                        }else{
                            content = danmuCommonService.setShowNotArrayContent(request.getParameter(key),type);
                            msgContent = danmuCommonService.setShowNotArrayContent(request.getParameter(key),componentId,type);
                            if(!"1".equals(componentId) && !"2".equals(componentId)){
                                restResultModel = checkDanmuIsOk(String.valueOf(msgContent));
                                if(restResultModel!=null){

                                    logger.info("屏蔽的消息内容:{}",msgContent);
                                    //return restResultModel;
                                    isBlock=true;
                                }
                            }

                        }
                    }
                }
                if(isCheck==0){
                    checkCmdTempComponentData = cmdTempComponentData;
                    bCheck = true;
                }
                map.put(key,content);
            }

            //是否保存弹幕
            DanmuPool danmuPool = findDanmuPool(addressId, partyId);
            //弹幕池编号
            String poolId = danmuPool.getId();
            Danmu danmuModel = new Danmu();
            danmuModel.setTemplateId(templateId);
            //danmuModel.setMsg(msg);
            danmuModel.setContent(map);
            danmuModel.setBlocked(isBlock);
            danmuModel.setCreateTime(date);
            danmuModel.setUpdateTime(date);
            danmuModel.setCreateUserId(wechatUser.getUserId());
            danmuModel.setUpdateUserId(wechatUser.getUserId());
            danmuModel.setDanmuPoolId(poolId);
            danmuModel.setDanmuSrc(danmuSrc);
            danmuModel.setViewFlg(false);

            danmuModel.setTime(time);
            //弹幕类型
            danmuModel.setType(danmuType);

            //是否保存弹幕
            if(isInDanmuLib==0){
               danmuModel = danmuService.save(danmuModel);
            }

            DanmuLog danmuLog = saveDanmuModel(danmuModel);


            if(isBlock){
                restResultModel = new RestResultModel();
                restResultModel.setResult(403);
                restResultModel.setResult_msg("发送的弹幕含有敏感词!");
                return restResultModel;
            }

            if(bCheck){

                //发送弹幕到
                logger.info("发送给服务器的弹幕信息:{}",JSON.toJSONString(danmuLog));
                sendDanmuToMq(partyId, addressId,danmuLog,wechatUser,party.getType(),cname,checkCmdTempComponentData);

            }else{
                CmdTemp cmdTemp = cmdTempService.findById(danmuLog.getTemplateId());
                Map<String,Object> commandObject = new HashMap<String,Object>();
                commandObject.put("type",cmdTemp.getKey());
                commandObject.put("data",danmuLog.getContent());
                pubMessageCollectorServer(partyId,addressId,commandObject);
            }
        }

        restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        restResultModel.setResult_msg("OK");
        return restResultModel;
    }


    private DanmuPool findDanmuPool(String addressId, String partyId){
        DanmuPool  danmuPool = danmuPoolService.findDanmuPoolbyPartyIdAndAddressId(addressId,partyId);
        if(danmuPool==null){
            danmuPool = danmuPoolService.saveDanmuPool(partyId,addressId);
        }
        return danmuPool;
    }

    /**
     * 管理员发动弹幕逻辑处理
     * @param request
     * @param userId
     * @param danmuSrc
     * @param danmuType
     * @return
     */
    public RestResultModel sendDanmuByAdmin(HttpServletRequest request,String userId,int danmuSrc,int danmuType,String adminId){

        RestResultModel restResultModel = new RestResultModel();
        //模板编号
        String templateId = request.getParameter("templateId");
        //活动编号
        String partyId = request.getParameter("partyId");
        //地址编号
        String addressId = request.getParameter("addressId");
        logger.info("指令编号:{},openId:{},活动编号:{},地址编号:{},弹幕来源:{},弹幕类型:{}",templateId,userId,partyId,addressId,danmuSrc,danmuType);
        //组件信息
        CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByIdFromCache(templateId);
        String name =cmdTempAllData.getName();

        Map<String,Object> contentMap = new HashMap<String,Object>();
        contentMap.put("danmuName",name);
        contentMap.put("color","");
        contentMap.put("content","");

        //是否入弹幕库 0入库  1不入库
        int isInDanmuLib = cmdTempAllData.getIsInDanmuLib()==null?1:cmdTempAllData.getIsInDanmuLib();
        Date date = DateUtils.getCurrentDate();
        PartyLogicModel party = rpcPartyService.findPartyByAddressId(addressId);
        if(party==null){
            logger.info("当前场地没有活动正在进行");
            restResultModel.setResult(404);
            restResultModel.setResult_msg("没有活动正在进行");
            return restResultModel;
        }
        //获取弹幕的时间
        int time = calculateDanmuTime(party,date);

        Object msg = null;
        boolean bCheck=false;
        boolean isBlock = false;

        CmdTempComponentData checkCmdTempComponentData = null;
        //模板下的所有组件
        List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
        Map<String,Object> map = new HashMap<String,Object>();
        if(ListUtils.checkListIsNotNull(cmdTempComponentDataList)){
            for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                String key = cmdTempComponentData.getKey();
                //数据类型
                //0数字 1布尔值 2字符串 3数组
                int type = cmdTempComponentData.getType();

                //组件的id 0无组件 1特效视频 2特效图片 3表情图片
                String componentId = cmdTempComponentData.getComponentId();

                int isCheck  = cmdTempComponentData.getIsCheck();

                //组件的类型 0text 1textarea 2select  3radiobutton 4checkbox
                Integer componentType = cmdTempComponentData.getComponentType();
                Object content = null;
                Object msgContent = null;
                boolean specialCompontentBoolean =  danmuCommonService.checkSpecialComponent(componentId);
                if(specialCompontentBoolean && "0".equals(componentId)){
                    map.put(key,cmdTempComponentData.getDefaultValue());
                    contentMap.put("content",cmdTempComponentData.getDefaultValue());
                }else{
                    if(type==3){
                        //显示的内容
                        content = danmuCommonService.setProtocolArrayContent(componentType,request.getParameter(key),cmdTempComponentData.getDefaultValue());
                        msgContent = danmuCommonService.setShowArrayContent(componentType,request.getParameter(key),componentId,cmdTempComponentData.getDefaultValue());
                        if("color".equals(key) ){
                            contentMap.put("color",request.getParameter(key+"name"));
                        }
                        if(msgContent!=null && (!"1".equals(componentId) && !"2".equals(componentId))){
                            List<String> messageList = (List<String>)msgContent;
                            for(int i=0; i<messageList.size(); i++){
                                restResultModel = checkDanmuIsOk(messageList.get(i));
                                if(restResultModel!=null){
                                    //return restResultModel;
                                    isBlock = true;
                                }
                            }
                        }
                    }else{
                        if("color".equals(key)){
                            String color = request.getParameter(key);
                            if(StringUtils.isEmpty(color)){
                                content = bmsColorService.getRandomColor();
                            }else{
                                content = danmuCommonService.setShowNotArrayContent(request.getParameter(key),type);
                            }
                            contentMap.put("color",request.getParameter(key+"name"));
                        }else{
                            content = danmuCommonService.setShowNotArrayContent(request.getParameter(key),type);
                            msgContent = danmuCommonService.setShowNotArrayContent(request.getParameter(key),componentId,type);
                            if("message".equals(key) || "idd".equals(key) || "expression".equals(key)  ){
                                contentMap.put("content",msgContent);
                            }
                            if(!"1".equals(componentId) && !"2".equals(componentId)){
                                restResultModel = checkDanmuIsOk(String.valueOf(msgContent));
                                if(restResultModel!=null){
                                    logger.info("屏蔽的消息内容:{}",msgContent);
                                    //return restResultModel;
                                    isBlock = true;
                                }
                            }
                        }
                    }
                }
                if(isCheck==0){
                    checkCmdTempComponentData = cmdTempComponentData;
                    bCheck = true;
                }
                map.put(key,content);
            }

            //是否保存弹幕
            DanmuPool danmuPool = danmuPoolService.findDanmuPoolbyPartyIdAndAddressId(addressId, partyId);
            //弹幕池编号
            String poolId = danmuPool.getId();
            Danmu danmuModel = new Danmu();
            danmuModel.setTemplateId(templateId);
            //danmuModel.setMsg(msg);
            danmuModel.setContent(map);
            danmuModel.setBlocked(isBlock);
            danmuModel.setCreateTime(date);
            danmuModel.setUpdateTime(date);
            danmuModel.setCreateUserId(userId);
            danmuModel.setUpdateUserId(userId);
            danmuModel.setDanmuPoolId(poolId);
            danmuModel.setDanmuSrc(danmuSrc);
            danmuModel.setViewFlg(false);

            danmuModel.setTime(time);
            //弹幕类型
            danmuModel.setType(danmuType);

            //是否保存弹幕
            if(isInDanmuLib==0){
                danmuModel = danmuService.save(danmuModel);
            }

            //记录弹幕历史
            DanmuLog danmuLog = saveDanmuModel(danmuModel);

            if(isBlock){
                restResultModel = new RestResultModel();
                restResultModel.setResult(403);
                restResultModel.setResult_msg("发送的弹幕含有敏感词!");
                return restResultModel;
            }

            if(bCheck){
                //发送弹幕到
                logger.info("发送给服务器的弹幕信息:{}",JSON.toJSONString(danmuLog));

                sendDanmuToMq(partyId, addressId,danmuLog,null,party.getType(),name,checkCmdTempComponentData);

            }else{

                CmdTemp cmdTemp = cmdTempService.findById(danmuLog.getTemplateId());
                Map<String,Object> commandObject = new HashMap<String,Object>();
                commandObject.put("type",cmdTemp.getKey());
                commandObject.put("data",danmuLog.getContent());
                pubMessageCollectorServer(partyId,addressId,commandObject);
            }
        }
        saveLog("A_advanceDanmu",partyId,addressId,contentMap,adminId);
        restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        restResultModel.setResult_msg("OK");
        return restResultModel;
    }


    private DanmuLog saveDanmuModel(Danmu danmuModel){
        DanmuLog danmuLog = new DanmuLog();
        BeanUtils.copyProperties(danmuModel,danmuLog);
        danmuLog.setId(null);
        if(danmuModel.getId()!=null){
            danmuLog.setDanmuId(danmuModel.getId());
        }
        return  danmuLogService.save(danmuLog);

    }

    /**
     * 发送弹幕到消息队列
     *
     * @param partyId
     * @param addressId
     * @param danmuModel
     * @param wechatUser
     */
    public void sendDanmuToMq(String partyId, String addressId, DanmuLog danmuModel, WechatUser wechatUser,int partyType,String cmdName,CmdTempComponentData checkCmdTempComponentData) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",danmuModel.getId());
        map.put("partyId",partyId);
        map.put("addressId",addressId);
        map.put("poolId",danmuModel.getDanmuPoolId());
        map.put("cmdName",cmdName);
        Object objectMsg = null;
        String value = danmuModel.getContent().get(checkCmdTempComponentData.getKey())+"";
        if(checkCmdTempComponentData.getType()==3){
            //显示的内容
            objectMsg = danmuCommonService.setShowArrayContent(checkCmdTempComponentData.getComponentType(),value,checkCmdTempComponentData.getComponentId(),checkCmdTempComponentData.getDefaultValue());
        }else{
            objectMsg = danmuCommonService.setShowNotArrayContent(value,checkCmdTempComponentData.getComponentId(),checkCmdTempComponentData.getType());
        }
        map.put("msg",objectMsg);
        map.put("type","normalDanmu");
        map.put("createTime",danmuModel.getCreateTime());
        map.put("dataType",checkCmdTempComponentData.getType());
        /**弹幕类型 0:普通弹幕.1:语音弹幕,2:视频,3:图片4：表情*/
        map.put("danmuType",checkCmdTempComponentData.getComponentId());

        if (wechatUser != null) {
            map.put("imgUrl",wechatUser.getImgUrl());
            map.put("sex",wechatUser.getSex());
            map.put("openId",wechatUser.getOpenId());
            map.put("nick",wechatUser.getNick());
        }

        //推动消息给服务器
        pubMessageToCheckServer(partyType,partyId,map);
    }



    /**
     * 推动消息给审核服务器
     * @param partyType
     * @param messageObject
     */
    public void pubMessageToCheckServer(int partyType,String partyId,Map<String,Object> messageObject){
        if(partyType==0){
            //上传弹幕到活动队列
            String key = DanmuCacheKey.SEND_DANMU_CACHE_LIST + partyId;
            String message = JSON.toJSONString(messageObject);
            redisService.setValueToList(key, message);
            logger.info("推送给活动审核界面弹幕信息:{},{}",partyId,message);
            redisService.expire(key, 60 * 60 * 1);
            redisTemplate.convertAndSend("partyId:danmu", partyId);
        }else{
            String key = DanmuCacheKey.SEND_FILM_DANMU_CACHE_LIST;
            String message = JSON.toJSONString(messageObject);
            redisService.setValueToList(key, message);
            logger.info("推送给电影审核界面端弹幕信息:{},{}",partyId,message);
            redisService.expire(key, 60 * 60 * 1);
            redisTemplate.convertAndSend("filmId:danmu", partyId);
        }
    }


    /**
     * 推动消息给弹幕服务器
     * @param partyId
     * @param addressId
     * @param messageObject
     */
    public void pubMessageCollectorServer(String partyId,String addressId,Map<String,Object> messageObject){

        logger.info("弹幕审核状态:{},直接广播给客户端:{}",JSON.toJSONString(messageObject));

        String key = DanmuCacheKey.PUB_DANMU_CACHE_LIST + addressId;
        redisService.setValueToList(key, JSON.toJSONString(messageObject));
        redisService.expire(key, 60 * 60 * 1);
        redisTemplate.convertAndSend("addressId:danmu", addressId);

    }


    /**
     * 计算弹幕开始的时间
     * @param partyLogicModel
     * @param nowTime
     * @return
     */
    public int  calculateDanmuTime(PartyLogicModel partyLogicModel,Date nowTime){
        int time  = 0;
        //电影开始时间
        Date startTime = partyLogicModel.getActiveTime();
        if (startTime != null) {
            time = (int) (nowTime.getTime() - startTime.getTime()) / 1000;
            logger.info("电影开始,当前发送弹幕时间与活动开始差:{}秒", time);
        }
        return time;
    }


    public WechatUser getWechatUserInfo(String openId){

        RestResultModel restResultModel = new RestResultModel();
        WechatUser wechatUser = null;
        if(!StringUtils.isEmpty(openId)){
            wechatUser = wechatUserService.findByOpenId(openId);
        }
        return wechatUser;
    }

    private void saveLog(String cmd,String partyId,String addressId,Map<String,Object> content,String adminId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                rpcOperationRpcLogService.insertOperationLogOfParty(cmd,partyId,addressId,adminId,content);
            }
        }).start();
    }

}
