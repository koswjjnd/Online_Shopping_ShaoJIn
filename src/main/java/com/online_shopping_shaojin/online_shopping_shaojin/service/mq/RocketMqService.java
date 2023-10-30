package com.online_shopping_shaojin.online_shopping_shaojin.service.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RocketMqService {
    @Resource
    private RocketMQTemplate template;

    public void sendMessage(String topic, String messageBody) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {//producer, receiver在consumerListener
        Message msg=new Message(topic, messageBody.getBytes());
        template.getProducer().send(msg);
    }

    public void sendDelayedMessage(String topic, String messageBody, int delayLevel) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message msg=new Message(topic, messageBody.getBytes());
        // messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        msg.setDelayTimeLevel(delayLevel);
        template.getProducer().send(msg);
    }
}
