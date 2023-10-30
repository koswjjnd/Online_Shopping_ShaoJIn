package com.online_shopping_shaojin.online_shopping_shaojin.service.mq;

import com.alibaba.fastjson.JSON;
import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingCommodityDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingOrderDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingOrder;
import com.online_shopping_shaojin.online_shopping_shaojin.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@Component//不用service因为这里不需要被调用
@Slf4j
@RocketMQMessageListener(topic="checkOrder",consumerGroup = "checkOrderGroup")
public class CheckOrderListener implements RocketMQListener<MessageExt> {
    @Resource
    OnlineShoppingOrderDao orderDao;
    @Resource
    OnlineShoppingCommodityDao commodityDao;

    @Resource
    RedisService redisService;
    @Override
    public void onMessage(MessageExt messageExt) {
        String body=new String(messageExt.getBody(), Charset.defaultCharset());
        log.info("Received Message from checkOrder, content:"+body);
        OnlineShoppingOrder orderMsg = JSON.parseObject(body, OnlineShoppingOrder.class);
        OnlineShoppingOrder orderDb = orderDao.queryOrderByNumber(orderMsg.getOrderNo());

        if (orderDb == null) {
            log.error("Can't find order in DB");
            return;
        }
        // 1. check current Order status in DB
        // Status as below:
        // 0. Invalid order, Since no available stock
        // 1. already create order, pending for payment
        // 2. finished payment
        // 99. invalid order due to payment proceed overtime
        if (orderDb.getOrderStatus() != 2) {
            //2. change order status to 99, invalid the order
            log.info("Didn't pay the order on time, order number："
                    + orderDb.getOrderNo());
            orderDb.setOrderStatus(99);
            orderDao.update(orderDb);
            //3. Update Commodity Table
            commodityDao.revertStock(orderDb.getCommodityId());//数据库把商品库存加1
            //4. Update Redis stock
            String redisKey = "Commodity:" + orderDb.getCommodityId();
            redisService.revertStock(redisKey);//redis里也要把商品库存加1
            redisService.removeFromDenyList(orderDb.getUserId(),
                    orderDb.getCommodityId());
        } else {
            log.info("Skip operation for order:" +
                    JSON.toJSON(orderDb));
        }
    }
}


