package com.hts.inf.nettylearn.domain;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * Created by IntelliJ IDEA.
 * User: jam
 * Date: 2019/12/22
 * Time: 下午12:46
 */
@Slf4j
@Data
public class ResponseMessage {

    Long seq;
    String body;

    public void encode(ByteBuf buffer) {
        buffer.writeLong(seq);
        buffer.writeBytes(body.getBytes());
    }

    public void decode(ByteBuf byteBuf) {
        seq = byteBuf.readLong();
        body = byteBuf.toString(StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "seq=" + seq +
                ", body='" + body + '\'' +
                '}';
    }
}
