package com.hts.inf.nettylearn.client.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by IntelliJ IDEA.
 * User: jam
 * Date: 2019/12/22
 * Time: 下午12:03
 */
public class FrameEncoderHandler extends LengthFieldPrepender {

    public FrameEncoderHandler() {
        super(2);
    }
}
