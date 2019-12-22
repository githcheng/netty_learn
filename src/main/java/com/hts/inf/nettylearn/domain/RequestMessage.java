package com.hts.inf.nettylearn.domain;

import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * Created by IntelliJ IDEA.
 * User: jam
 * Date: 2019/12/22
 * Time: 下午12:32
 */
@Data
public class RequestMessage {

    Long seq;
    private String body;

    public void decode(ByteBuf byteBuf) {
        seq = byteBuf.readLong();
        body = byteBuf.toString(StandardCharsets.UTF_8);
    }

    public void encode(ByteBuf buffer) {
        buffer.writeLong(seq);
        buffer.writeBytes(body.getBytes());
    }
}
