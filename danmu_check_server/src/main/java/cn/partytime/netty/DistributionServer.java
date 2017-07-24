package cn.partytime.netty;

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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by user on 16/6/22.
 */

@Component
public class DistributionServer {

    private static final Logger logger = LoggerFactory.getLogger(DistributionServer.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    ExecutorService executorService = Executors.newCachedThreadPool();

    @Value("${netty.port:9090}")
    private int port;

    @Autowired
    private DistributionServerInitializer danmuServerInitializer;

    /**
     * 启动系统加载项目
     */
    @PostConstruct
    public void init() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                nettyStart();
            }
        });
    }
    private void nettyStart() {
        logger.info("netty服务启动!");
        try {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(danmuServerInitializer)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定端口，开始接收进来的连接
            ChannelFuture f = serverBootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @PreDestroy
    private void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

}
