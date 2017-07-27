package cn.partytime.service;

import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.dataRpc.RpcCmdService;
import cn.partytime.dataRpc.RpcDanmuService;
import cn.partytime.handlerThread.TestDanmuHandler;
import cn.partytime.common.cachekey.FunctionControlCacheKey;
import cn.partytime.common.util.BooleanUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.CmdTempComponentData;
import cn.partytime.model.DanmuModel;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lENOVO on 2016/10/11.
 */
@Component
public class TestDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(TestDanmuService.class);

    @Autowired
    private RpcDanmuService rpcDanmuService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TestDanmuHandler testDanmuHandler;

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;


    @Autowired
    private RpcCmdService rpcCmdService;


    public void sendTestDanmu(String addressId, String partyId) {
        List<DanmuModel> danmuModelPage = rpcDanmuService.findDanmuByIsBlocked(0, 200, false);
        Iterator<DanmuModel> danmuModelIterator = danmuModelPage.iterator();
        int count = 0;
        while (danmuModelIterator.hasNext()) {
            count++;
            DanmuModel danmuModel = danmuModelIterator.next();
            //每隔一秒发送一个弹幕
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Object danmuModelObject = redisService.get(FunctionControlCacheKey.FUNCITON_CONTROL_TESTMODEL + partyId);
            if (BooleanUtils.objectConvertToBoolean(danmuModelObject)) {
                //被删除的弹幕不作为测试弹幕
                //if (danmuModel.getIsDelete() == 0) {
                Map<String,Object> map = new HashMap<>();


                map.put("type","testDanmu");
                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("id",danmuModel.getId());
                dataMap.put("partyId",partyId);
                dataMap.put("addressId",addressId);
                dataMap.put("poolId",danmuModel.getDanmuPoolId());
                //dataMap.put("msg",danmuModel.getMsg());
                dataMap.put("type","testDanmu");
                dataMap.put("createTime",danmuModel.getCreateTime());

                CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByIdFromCache(danmuModel.getTemplateId());

                Map<String,Object> contentMap = danmuModel.getContent();
                List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
                CmdTempComponentData cmdTempComponentData=null;
                for(CmdTempComponentData cmdTempComponentDataTemp:cmdTempComponentDataList){
                    int isCheck = cmdTempComponentDataTemp.getIsCheck();
                    if(isCheck==0){
                        cmdTempComponentData = cmdTempComponentDataTemp;

                        dataMap.put("msg",danmuModel.getContent().get(cmdTempComponentData.getKey()));
                    }
                }

                dataMap.put("dataType",cmdTempComponentData.getType());
                dataMap.put("danmuType",cmdTempComponentData.getComponentId());
                map.put("data",dataMap);
                List<Channel> channelList = danmuChannelRepository.findChannelListByPartyId(partyId);

                if (ListUtils.checkListIsNotNull(channelList)) {
                    for (Channel channel : channelList) {
                        channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(map)));
                    }
                }

                logger.info("当前count：" + count);
                if (count == 200) {
                    count = 0;
                    logger.info("当前测试弹幕已发送完毕，重启启动一批");
                    testDanmuHandler.danmuListenHandler(addressId, partyId);
                }
            } else {
                logger.info("管理员关闭{}活动的测试模式", partyId);
                break;
            }
        }
    }

}
