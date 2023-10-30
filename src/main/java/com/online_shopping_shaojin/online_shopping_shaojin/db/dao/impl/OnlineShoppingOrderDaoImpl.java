package com.online_shopping_shaojin.online_shopping_shaojin.db.dao.impl;

import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingOrderDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.mappers.OnlineShoppingCommodityMapper;
import com.online_shopping_shaojin.online_shopping_shaojin.db.mappers.OnlineShoppingOrderMapper;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingOrder;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingShardingOrder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class OnlineShoppingOrderDaoImpl implements OnlineShoppingOrderDao {
    @Resource
    OnlineShoppingOrderMapper mapper;
    @Override
    public int insertOrder(OnlineShoppingOrder order) {
        return mapper.insert(order);
    }

    @Override
    public int insertOrderSharding(OnlineShoppingShardingOrder order) {
        return mapper.insertOrderSharding(order);
    }

    @Override
    public int update(OnlineShoppingOrder order) {
        return mapper.updateByPrimaryKey(order);
    }

    @Override
    public OnlineShoppingOrder queryOrder(Long orderId) {
        return mapper.selectByPrimaryKey(orderId);
    }

    @Override
    public OnlineShoppingOrder queryOrderSharding(Long orderId, Long userID) {
        return mapper.selectByUserIdOrderID(orderId, userID);
    }

    @Override
    public int deleteOder(Long orderId) {
        return mapper.deleteByPrimaryKey(orderId);
    }

    @Override
    public OnlineShoppingOrder queryOrderByNumber(String orderNumber) {
        return mapper.queryOrderByNumber(orderNumber);//去蓝鸟头mapper里加这个方法
    }
}
