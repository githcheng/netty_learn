package com.hts.inf.nettylearn.client.dispatcher;

import com.hts.inf.nettylearn.domain.ResponseMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: jam
 * Date: 2019/12/22
 * Time: 下午3:55
 */
public class RequestPendingCenter {
    private Map<Long,ResultFuture> map = new ConcurrentHashMap<>();


    public void add(Long requestId, ResultFuture resultFuture){
        this.map.put(requestId,resultFuture);
    }

    public void set(Long requestId,ResponseMessage responseMessage){
        ResultFuture future = this.map.get(requestId);
        if (future != null){
            future.setSuccess(responseMessage);
            map.remove(requestId);
        }
    }
}
