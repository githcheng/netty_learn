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
package com.hts.inf.nettylearn.server;

import com.hts.inf.nettylearn.domain.RequestMessage;
import com.hts.inf.nettylearn.domain.ResponseMessage;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler implementation for the echo server.
 */
@Sharable
@Slf4j
public class EchoServerHandler extends SimpleChannelInboundHandler<RequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RequestMessage requestMessage) throws Exception {

        String body = requestMessage.getBody();
        // 处理逻辑
        log.info(body);
        String result = "ok, received: "+body;

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setSeq(requestMessage.getSeq());
        responseMessage.setBody(result);

        channelHandlerContext.writeAndFlush(responseMessage);
    }
}