package com.online_shopping_shaojin.online_shopping_shaojin.db.mappers;

import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingOrder;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingShardingOrder;

public interface OnlineShoppingOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(OnlineShoppingOrder record);

    int insertSelective(OnlineShoppingOrder record);

    OnlineShoppingOrder selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(OnlineShoppingOrder record);

    int updateByPrimaryKey(OnlineShoppingOrder record);

    OnlineShoppingOrder queryOrderByNumber(String orderNumber);//去红鸟头里加这个方法

    OnlineShoppingOrder selectByUserIdOrderID(Long orderId, Long userId);

    int insertOrderSharding(OnlineShoppingOrder record);
}