package com.hts.inf.nettylearn.server;

import com.hts.inf.nettylearn.server.codec.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;



/**
 * Created by IntelliJ IDEA.
 * User: jam
 * Date: 2019/12/13
 * Time: 下午9:22
 */
@Slf4j
@Service
public class OriginNettyEchoServer implements InitializingBean {

    static final int PORT = Integer.parseInt(System.getProperty("port", "9001"));

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("==== OriginNettyEchoServer starting ====");
                    start();
                } catch (InterruptedException e) {
                    log.info("==== OriginNettyEchoServer start failed!!! ====");
                    return;
                }
            }
        }).start();
        log.info("==== OriginNettyEchoServer success!!! ====");
    }

    private void start() throws InterruptedException {

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
        EventLoopGroup workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("work"));
        final EchoServerHandler serverHandler = new EchoServerHandler();
        MetricHandler metricHandler = new MetricHandler();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            p.addLast("frameDecoderHandler",new FrameDecoderHandler());
                            p.addLast("frameEncoderHandler",new FrameEncoderHandler());

                            p.addLast("protocolDecoderHandler",new ProtocolDecoderHandler());
                            p.addLast("protocolEncoderHandler",new ProtocolEncoderHandler());

                            p.addLast("metricHandler",metricHandler);
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast("serverHandler",serverHandler);
                        }
                    });

            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * sever 单独测试
     * @param args
     */
    public static void main(String[] args) {
        OriginNettyEchoServer server = new OriginNettyEchoServer();
        try {
            server.start();
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
    }
}
