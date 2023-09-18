package com.online_shopping_shaojin.online_shopping_shaojin.service;


import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingCommodityDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingOrderDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service//新建一个service的目的是
public class OrderService {
    @Resource
    OnlineShoppingCommodityDao commodityDao;
    @Resource
    OnlineShoppingOrderDao orderDao;
    public OnlineShoppingOrder processOrder(Long userId, Long commodityId){
        OnlineShoppingCommodity commodity = commodityDao.queryCommodityById(commodityId);
        Integer availableStock = commodity.getAvailableStock();//用option加回车自动生产local variable

        if (availableStock>0) {
            commodity.setAvailableStock(availableStock-1);
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
}
