package com.hts.inf.nettylearn.client.dispatcher;

import com.hts.inf.nettylearn.domain.ResponseMessage;
import io.netty.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: jam
 * Date: 2019/12/22
 * Time: 下午3:54
 */
public class ResultFuture extends DefaultPromise<ResponseMessage> {

    private static EventExecutor executor = new UnorderedThreadPoolEventExecutor(1
            , new DefaultThreadFactory("ResultFutureFactory"));

    public ResultFuture() {
        super(executor);
    }
}
