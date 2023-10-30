package com.online_shopping_shaojin.online_shopping_shaojin.db.dao;

import com.online_shopping_shaojin.online_shopping_shaojin.db.mappers.OnlineShoppingCommodityMapper;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class OnlineShoppingCommodityDaoTest {
    @Resource
    OnlineShoppingCommodityDao dao;

    @Test
    void deleteCommodityById() {
        dao.deleteCommodityById(123L);
    }

    @Test
    void insertCommodity() {
        dao.deleteCommodityById(123L);
        OnlineShoppingCommodity commodity=OnlineShoppingCommodity.builder()
                .commodityId(123L)
                .commodityDesc("test product")
                .availableStock(4)
                .totalStock(10)
                .lockStock(0)
                .price(120)
                .commodityName("iphone")
                .creatorUserId(4L)
                .build();
        dao.insertCommodity(commodity);
    }

    @Test
    void queryCommodityById() {
        OnlineShoppingCommodity commodity = dao.queryCommodityById(123L);
        if (commodity != null) {
            log.info(commodity.getCommodityName());
        }
    }

    @Test
    void updateCommodity() {
        OnlineShoppingCommodity commodity=OnlineShoppingCommodity.builder()
                .commodityId(123L)
                .commodityDesc("test product 123")
                .availableStock(4)
                .totalStock(10)
                .lockStock(0)
                .price(120)
                .commodityName("iphone 123")
                .creatorUserId(4L)
                .build();
        dao.updateCommodity(commodity);
    }

    @Test
    void listCommoditiesByUserId() {
        List<OnlineShoppingCommodity> onlineShoppingCommodities = dao.listCommoditiesByUserId(4L);
        log.info(onlineShoppingCommodities.size()+" ");

    }
}