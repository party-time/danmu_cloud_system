package cn.partytime.netty;

import cn.partytime.common.constants.ClientConst;
import cn.partytime.service.ClientLoginService;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by lENOVO on 2016/12/6.
 */

@Component
@Qualifier("webSocketServerHandler")
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);

    private WebSocketServerHandshaker handshaker;


    @Autowired
    private ClientLoginService clientLoginService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (request instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) request);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

        String url = req.uri();
        logger.info("建立连接url:{}",url);
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url);
        Map<String, List<String>> parameters = queryStringDecoder.parameters();

        logger.info("参数数量:"+parameters.size());
        logger.info("parameters.size()>=2:"+(parameters.size()>=2));
        if (parameters.size()<2) {
            logger.info("参数不可缺省");
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND));
            return;
        }
        String code = parameters.get("code").get(0);
        String clientType = parameters.get("clientType").get(0);



        if(StringUtils.isEmpty(clientType) || StringUtils.isEmpty(code)){
            logger.info("唯一标识,客户端类型都不能为空");
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND));
            return;
        }

        // Handshake
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null, true);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            ChannelFuture channelFuture = handshaker.handshake(ctx.channel(), req);
            if (ClientConst.CLIENT_TYPE_WECHATMIN.equals(clientType)) {
                String latitude = parameters.get("latitude").get(0);
                String longitude = parameters.get("longitude").get(0);
                clientLoginService.WechatMinLogin(code,clientType,ctx.channel(),latitude,longitude);
            }else {
                clientLoginService.clientLogin(code,clientType,ctx.channel());
            }
        }
    }

    private static String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HOST) + req.uri();
        return "ws://" + location;
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

}
