package com.online_shopping_shaojin.online_shopping_shaojin.service;

import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingCommodityDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class RedisPreHeat implements ApplicationRunner {//一启动springboot就直接启用这个方法：把database数据全部读出来放到redis里头
    @Resource
    OnlineShoppingCommodityDao commodityDao;
    @Resource
    RedisService redisService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //read
        List<OnlineShoppingCommodity> onlineShoppingCommodities = commodityDao.listCommoditiesByUserId(4L);//应该返回全部commodity但现在没这个方法
    //write
        for (OnlineShoppingCommodity commodity: onlineShoppingCommodities){
            String key="Commodity:"+commodity.getCommodityId();
            redisService.set(key, commodity.getAvailableStock().toString());
            log.info("Preheat for commodity:"+commodity.getCommodityId());
        }


    }
}
