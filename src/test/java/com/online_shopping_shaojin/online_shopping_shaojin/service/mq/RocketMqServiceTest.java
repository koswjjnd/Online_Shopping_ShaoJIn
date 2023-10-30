package com.online_shopping_shaojin.online_shopping_shaojin.service.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest//帮我把springboot启动起来这样才能用下面的resource(所有test都要加这一行）
class RocketMqServiceTest {
    @Resource
    RocketMqService service;
    @Test
    void sendMessage() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        service.sendMessage("Zhangsan","Hello, Zhangsan");
    }
    @Test
    void sendDelayedMessage() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        service.sendDelayedMessage("Zhangsan","Hello, delayed Zhangsan",100);
    }
}