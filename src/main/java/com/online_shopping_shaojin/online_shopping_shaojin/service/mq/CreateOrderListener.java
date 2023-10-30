package com.online_shopping_shaojin.online_shopping_shaojin.service.mq;

import com.alibaba.fastjson.JSON;
import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingCommodityDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingOrderDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;


@Component//不用service因为这里不需要被调用
@Slf4j
@RocketMQMessageListener(topic="createOrder",consumerGroup = "CreateOrderGroup")
public class CreateOrderListener implements RocketMQListener<MessageExt> {
    @Resource
    OnlineShoppingCommodityDao commodityDao;
    @Resource
    OnlineShoppingOrderDao orderDao;
    @Resource
    RocketMqService rocketMqService;
    @Override
    public void onMessage(MessageExt messageExt) {
        String message=new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("createOrder Message Body:"+message);
        OnlineShoppingOrder onlineShoppingOrder = JSON.parseObject(message, OnlineShoppingOrder.class);

        int res = commodityDao.deductStock(onlineShoppingOrder.getCommodityId());
        if (res>0){
            OnlineShoppingCommodity commodity = commodityDao.queryCommodityById(onlineShoppingOrder.getCommodityId());
            onlineShoppingOrder.setOrderStatus(1);
            onlineShoppingOrder.setCreateTime(new Date());
            onlineShoppingOrder.setOrderAmount(commodity.getPrice().longValue());
            orderDao.insertOrder(onlineShoppingOrder);
            //send delayed msg to checkOrder
            try {
                rocketMqService.sendDelayedMessage("checkOrder",JSON.toJSONString(onlineShoppingOrder),3);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }

    }
}