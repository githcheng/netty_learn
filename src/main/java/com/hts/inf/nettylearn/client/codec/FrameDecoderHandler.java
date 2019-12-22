package com.hts.inf.nettylearn.client.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: jam
 * Date: 2019/12/22
 * Time: 下午12:03
 */
public class FrameDecoderHandler extends LengthFieldBasedFrameDecoder {

    public FrameDecoderHandler() {
        super(Integer.MAX_VALUE,0,2,0,2);
    }
}
