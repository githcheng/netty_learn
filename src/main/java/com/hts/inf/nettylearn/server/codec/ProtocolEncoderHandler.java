package com.hts.inf.nettylearn.server.codec;

import com.hts.inf.nettylearn.domain.RequestMessage;
import com.hts.inf.nettylearn.domain.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: jam
 * Date: 2019/12/22
 * Time: 下午12:28
 */
public class ProtocolEncoderHandler extends MessageToMessageEncoder<ResponseMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ResponseMessage responseMessage, List<Object> list) throws Exception {
        ByteBuf buffer = channelHandlerContext.alloc().buffer();
        responseMessage.encode(buffer);
        list.add(buffer);
    }
}
