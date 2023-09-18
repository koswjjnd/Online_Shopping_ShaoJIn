package com.online_shopping_shaojin.online_shopping_shaojin.db.dao;

import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class OnlineShoppingOrderDaoTest {
    @Resource
    OnlineShoppingOrderDao dao;//增删查改都要用dao
    @Test
    void insertOrder() {
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
}