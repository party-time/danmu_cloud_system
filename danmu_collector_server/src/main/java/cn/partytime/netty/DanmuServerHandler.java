package cn.partytime.netty;

import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.model.DanmuClientModel;
import cn.partytime.service.PotocolService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by task on 2016/6/15.
 */
@Component
@Qualifier("danmuServerHandler")
@ChannelHandler.Sharable
public class DanmuServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(DanmuServerHandler.class);


    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private PotocolService potocolService;


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();
        logger.info("客户端:" + channel.id() + " 加入");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel channel = ctx.channel();
        channel.writeAndFlush("客户端:" + channel.id() + "在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        logger.info("客户端:" + channel.id() + "掉线");;
        potocolService.forceLogout(channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
        Channel channel = ctx.channel();
        logger.info("客户端:" + channel.id() + "异常");

        logger.info(cause.getMessage());
        // 当出现异常就关闭连接
        cause.printStackTrace();

        potocolService.forceLogout(channel);
    }

    /**
     * 监听客户端心跳
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //客户端发生异常。不做下线处理
        //potocolService.forceLogout(ctx.channel());
        Channel channel = ctx.channel();
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                DanmuClientModel danmuClientModel = danmuChannelRepository.get(channel);
                logger.info("长时间没有获取到客户端{}的内容踢下线",JSON.toJSONString(danmuClientModel));
                if (danmuClientModel.getClientType() == 2) {
                    potocolService.forceLogout(channel);
                }
            } else if (event.state() == IdleState.WRITER_IDLE) {
                logger.info("write idle");
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //然后初始化客户端
        Channel channel = ctx.channel();
        String accecptStr = textWebSocketFrame.text();
        /*ProtocolModel protocolModel = JSON.parseObject(accecptStr, ProtocolModel.class);*/
        logger.info("收到的消息:{}",accecptStr);
        Map<String,Object> map = (Map<String,Object>)JSON.parse(accecptStr);

        if("command".equals(String.valueOf(map.get("type")))){
            Map<String, Object> dataMap = (Map<String, Object>) JSON.parse(String.valueOf(map.get("data")));
            potocolService.potocolHandler(dataMap, channel);
        }else{
            //协议处理
            potocolService.potocolHandler(map, channel);
        }

    }
}
