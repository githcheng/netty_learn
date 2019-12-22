package com.hts.inf.nettylearn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class NettyLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyLearnApplication.class, args);
        log.info("======Application Started!!!======");
    }

}
