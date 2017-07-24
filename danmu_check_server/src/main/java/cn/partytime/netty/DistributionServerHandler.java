package cn.partytime.netty;

import cn.partytime.service.CommandHanderService;
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
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by user on 2016/6/15.
 */
@Component
@Qualifier("distributionServerHandler")
@ChannelHandler.Sharable
public class DistributionServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(DistributionServerHandler.class);

    @Autowired
    private CommandHanderService commandHanderService;


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        logger.info("客户端:" + ctx.channel().id() + " 加入");

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端:" + ctx.channel().id() + " 离开");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        if (StringUtils.isEmpty(msg.text())) {
            return;
        }
        Map<String, Object> map = (Map<String, Object>) JSON.parse(msg.text());
        //CommandModel commandModel = JSON.parseObject(msg.text(),CommandModel.class);
        commandHanderService.commandHandler(map, channel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel channel = ctx.channel();
        logger.info("客户端:" + channel.id() + "在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel channel = ctx.channel();
        logger.info("客户端:" + channel.id() + "掉线");
        commandHanderService.forceLogout(channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
        Channel channel = ctx.channel();
        logger.info("客户端:" + channel.id() + "异常");
        cause.printStackTrace();
        ctx.close();
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
        Channel channel = ctx.channel();
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                logger.info("没有接收到客户端发送的消息");
                commandHanderService.forceLogout(channel);
            } else if (event.state() == IdleState.WRITER_IDLE) {
                //logger.info("没有向客户端写入消息");
            }
        }
    }
}
