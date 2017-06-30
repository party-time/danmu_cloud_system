package cn.partytime.collector.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


/**
 * Created by task on 16/6/22.
 */

@Component
public class DanmuServer {

    private static final Logger logger = LoggerFactory.getLogger(DanmuServer.class);

    @Value("${netty.port:9090}")
    private int port;

    @Autowired
    private DanmuServerInitializer danmuServerInitializer;

    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 启动系统加载项目
     */
    @PostConstruct
    public void init() {

        try {
            logger.info("启动netty主线程");
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    nettyStart();
                }
            });
        }catch (Exception e){
            logger.error("线程启动异常:{}",e.getMessage());
        }

    }

    private void nettyStart() {
        logger.info("netty服务启动!");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(danmuServerInitializer)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("netty服务启动异常:{}", e.getMessage());
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
