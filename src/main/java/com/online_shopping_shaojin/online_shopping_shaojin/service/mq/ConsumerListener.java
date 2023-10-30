package com.online_shopping_shaojin.online_shopping_shaojin.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component//不用service因为这里不需要被调用
@Slf4j
@RocketMQMessageListener(topic="Zhangsan",consumerGroup = "ZhangsanGroup")
public class ConsumerListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        String msg=new String(messageExt.getBody(), Charset.defaultCharset());
        log.info("Received Message:", msg);
    }
}
