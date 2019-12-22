/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.hts.inf.nettylearn.client;

import com.hts.inf.nettylearn.client.codec.*;
import com.hts.inf.nettylearn.client.dispatcher.RequestPendingCenter;
import com.hts.inf.nettylearn.client.dispatcher.ResultFuture;
import com.hts.inf.nettylearn.domain.RequestMessage;
import com.hts.inf.nettylearn.domain.ResponseMessage;
import com.hts.inf.nettylearn.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;

import javax.sound.midi.Soundbank;
import java.util.Random;

@Slf4j
public final class EchoClient {

    public static final boolean SSL = System.getProperty("ssl") != null;
    public static final String HOST = System.getProperty("host", "127.0.0.1");
    public static final int PORT = Integer.parseInt(System.getProperty("port", "9001"));
    public static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.git
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        RequestPendingCenter requestPendingCenter = new RequestPendingCenter();

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();



        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc(), HOST, PORT));
                            }

                            p.addLast("frameEncoderHandler",new FrameEncoderHandler());
                            p.addLast("frameDecoderHandler",new FrameDecoderHandler());
                            p.addLast("protocolEncoderHandler",new ProtocolEncoderHandler());
                            p.addLast("protocolDecoderHandler",new ProtocolDecoderHandler());

                            p.addLast("responseDispatcherHandler",new ResponseDispatcherHandler(requestPendingCenter));
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    });

            ChannelFuture f = b.connect(HOST, PORT).sync();


            RequestMessage requestMessage = new RequestMessage();
            long seq = new Random().nextLong();
            requestMessage.setSeq(seq);
            requestMessage.setBody("hello");

            ResultFuture resultFuture = new ResultFuture();
            requestPendingCenter.add(seq,resultFuture);

            f.channel().writeAndFlush(requestMessage);

            ResponseMessage responseMessage = resultFuture.get();


            log.info("response:{}",responseMessage);
            System.out.println(responseMessage);

            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }
}