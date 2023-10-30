package com.online_shopping_shaojin.online_shopping_shaojin.db.dao;

import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingOrder;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingShardingOrder;
import com.online_shopping_shaojin.online_shopping_shaojin.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class OnlineShoppingOrderDaoTest {
    @Resource
    OnlineShoppingOrderDao dao;//增删查改都要用dao
    @Test
    void insertOrder() {
        dao.deleteOder(123L);
        OnlineShoppingOrder order= OnlineShoppingOrder.builder()
                .orderId(123L)
                .commodityId(123L)
                .build();
        dao.insertOrder(order);
    }

    @Test
    void update() {
    }

    @Test
    void queryOrder() {
    }

    @Test
    void deleteOder() {
    }


    @Test
    void testQueryOrderBySharding(){
        log.info("order id:102", dao.queryOrderSharding(102L, 123L));
        log.info("order id:103", dao.queryOrderSharding(103L, 123L));
    }
    @Test
    void testInsertOrderBySharding(){
        for (int i = 0; i < 100; i++) {
            long orderId = i + 100L;
            OnlineShoppingOrder order =
                    OnlineShoppingOrder.builder()
                            .orderStatus(0)
                            .orderNo("123")
                            .orderId(orderId)
                            .orderAmount(123L)
                            .commodityId(123L)
                            .createTime(new Date())
                            .payTime(new Date())
                            .userId(123L)
                            .orderStatus(0)
                            .build();
            dao.insertOrderSharding(order);
            //
        }
    }

    @Test
    void insert_order_schema_online_shopping_SnowFlake() {
        List<OnlineShoppingOrder> orders = new ArrayList<>();
        SnowflakeIdWorker snowFlake=new SnowflakeIdWorker(1,2);
        for (int i = 0; i < 100; i++) {
            long orderId = snowFlake.nextId();
            System.out.println(orderId);
            OnlineShoppingOrder order =
                    OnlineShoppingOrder.builder()
                            .orderStatus(0)
                            .orderNo("123")
                            .orderId(orderId)
                            .orderAmount(123L)
                            .commodityId(123L)
                            .createTime(new Date())
                            .payTime(new Date())
                            .userId(124L)
                            .orderStatus(0)
                            .build();
            orders.add(order);
        }
        for (int i = 0; i < 100; i++) {
            dao.insertOrderSharding(orders.get(i));
        }
    }
}