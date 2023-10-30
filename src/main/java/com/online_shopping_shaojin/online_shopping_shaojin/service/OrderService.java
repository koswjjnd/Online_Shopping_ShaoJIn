package com.online_shopping_shaojin.online_shopping_shaojin.service;


import com.alibaba.fastjson.JSON;
import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingCommodityDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingOrderDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingOrder;
import com.online_shopping_shaojin.online_shopping_shaojin.service.mq.RocketMqService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service//新建一个service的目的是
@Slf4j //处理了log。info
public class OrderService {
    @Resource
    OnlineShoppingCommodityDao commodityDao;
    @Resource
    OnlineShoppingOrderDao orderDao;
    @Resource
    RedisService redisService;

    @Resource
    RocketMqService rocketMqService;
    public OnlineShoppingOrder processOrder(Long userId, Long commodityId){//这个会产生超卖问题，同时100个人下单，还没执行第29行更新数据库就又查询了（执行第24行）
        OnlineShoppingCommodity commodity = commodityDao.queryCommodityById(commodityId);
        Integer availableStock = commodity.getAvailableStock();//用option加回车自动生产local variable

        if (availableStock>0) {
            commodity.setAvailableStock(availableStock-1);
            log.info("process order successful for commodityID"+ commodityId+", Current availableStock: "+availableStock);
            commodityDao.updateCommodity(commodity);
            OnlineShoppingOrder order = OnlineShoppingOrder.builder()
                    .userId(userId)
                    .orderNo(UUID.randomUUID().toString())
                    .commodityId(commodityId)
                    .orderAmount(Long.parseLong(commodity.getPrice().toString()))
                    .orderStatus(1)
                    .createTime(new Date())
                    .build();
            orderDao.insertOrder(order);
            return order;
        }
        log.info("Process fail due to no available stock for commodityID: "+ commodityId);
        return null;
    }
    public OnlineShoppingOrder processOrder_selectupdate_inonesql(Long userId, Long commodityId){//上面的修正版解决超卖问题
        OnlineShoppingCommodity commodity = commodityDao.queryCommodityById(commodityId);
        Integer availableStock = commodity.getAvailableStock();//用option加回车自动生产local variable

        if (availableStock>0) {

            int result = commodityDao.deductStock(commodityId);//在deductStock里面是update语句，他会返回值你更改了多少行，如果0说明没有更改
            if (result>0) {
                log.info("process order successful for commodityID"+ commodityId+", Current availableStock: "+availableStock);
                OnlineShoppingOrder order = OnlineShoppingOrder.builder()
                        .userId(userId)
                        .orderNo(UUID.randomUUID().toString())
                        .commodityId(commodityId)
                        .orderAmount(Long.parseLong(commodity.getPrice().toString()))
                        .orderStatus(1)
                        .createTime(new Date())
                        .build();
                orderDao.insertOrder(order);
                return order;
            }
        }
        log.info("Process fail due to no available stock for commodityID: "+ commodityId);
        return null;
    }
    public OnlineShoppingOrder processOrder_Store_procedure(Long userId, Long commodityId){//和上一个inonesql区别在于sp可以适用于更广泛的查询，比如你一个操作需要查询不同的两个表，同时要更改对应的两个表，并且仍然需要保证上面说的操作保持原子性，简单的sql语言就不够用了
        OnlineShoppingCommodity commodity = commodityDao.queryCommodityById(commodityId);
        Integer availableStock = commodity.getAvailableStock();//用option加回车自动生产local variable

        if (availableStock>0) {

            int result = commodityDao.deductStockWithStoreProcedure(commodityId);//在deductStock里面是update语句，他会返回值你更改了多少行，如果0说明没有更改
            if (result>0) {
                log.info("process order successful for commodityID"+ commodityId+", Current availableStock: "+availableStock);
                OnlineShoppingOrder order = OnlineShoppingOrder.builder()
                        .userId(userId)
                        .orderNo(UUID.randomUUID().toString())
                        .commodityId(commodityId)
                        .orderAmount(Long.parseLong(commodity.getPrice().toString()))
                        .orderStatus(1)
                        .createTime(new Date())
                        .build();
                orderDao.insertOrder(order);
                return order;
            }
        }
        log.info("Process fail due to no available stock for commodityID: "+ commodityId);
        return null;
    }

    public OnlineShoppingOrder processOrderRedis(Long userId, Long commodityId){//处理不同机器同时要读取信息的请求
        String key="Commodity:"+commodityId;//定义key for record remaining stock
        long currentStock = redisService.stockDeduct(key);

        if (currentStock>=0) {
            int result=commodityDao.deductStock(commodityId);
            if (result<=0){
                throw new RuntimeException("data sync error between redis and Mysql");
            }
            OnlineShoppingCommodity commodity = commodityDao.queryCommodityById(commodityId);
            log.info("process order successful for commodityID"+ commodityId+", Current availableStock: "+commodity.getAvailableStock());
            OnlineShoppingOrder order = OnlineShoppingOrder.builder()
                    .userId(userId)
                    .orderNo(UUID.randomUUID().toString())
                    .commodityId(commodityId)
                    .orderAmount(Long.parseLong(commodity.getPrice().toString()))
                    .orderStatus(1)
                    .createTime(new Date())
                    .build();
            orderDao.insertOrder(order);
            return order;
        }
        log.info("Process fail due to no available stock for commodityID: "+ commodityId);
        return null;
    }

    public OnlineShoppingOrder processOrderRedisAndRocketMQ(Long userId, Long commodityId) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {//处理不同机器同时要读取信息的请求和写入请求
        String key="Commodity:"+commodityId;//定义key for record remaining stock

        long currentStock = redisService.stockDeduct(key);//读取请求

        if (currentStock>=0) {
            redisService.addToDenyList(userId, commodityId);
            OnlineShoppingOrder order = OnlineShoppingOrder.builder()//建一个order传进messagequeue
                    .userId(userId)
                    .orderNo(UUID.randomUUID().toString())
                    .commodityId(commodityId)
                    .build();
            String msg= JSON.toJSONString(order);
            rocketMqService.sendMessage("createOrder", msg);//在consumer端一条一条的插入，即在createorderlistener里插入，避免sql崩溃
            return order;//现在这个order没有加在数据库里
        }
        log.info("Process fail due to no available stock for commodityID: "+ commodityId);
        return null;
    }
    public void payOrder(String orderNumber){
        OnlineShoppingOrder order=orderDao.queryOrderByNumber(orderNumber);//去dao里加这个新方法
        order.setOrderStatus(2);
        order.setPayTime(new Date());
        orderDao.update(order);
    }

    public OnlineShoppingOrder queryOrder(String orderNumber) {
        OnlineShoppingOrder order=orderDao.queryOrderByNumber(orderNumber);
        return order;
    }

    public OnlineShoppingOrder processOrder_DistributedLock(Long userId, Long commodityId){
        //add lock
        String key="LockCommodity:"+commodityId.toString();//distinguish between key for record remaining stock and key for lock
        String value=UUID.randomUUID().toString();
        boolean isSuccess = redisService.getDistributedLock(key, value, 10000);
        if (isSuccess) {
            OnlineShoppingOrder onlineShoppingOrder = processOrder(userId, commodityId);
            log.info("Successfuly ordered");
            //unlock
            redisService.releaseDistributedLock(key,value);
            return onlineShoppingOrder;
        }
        log.info("Please try later due to the distributed lock");
        return null;
    }
}
