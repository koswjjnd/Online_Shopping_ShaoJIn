package com.online_shopping_shaojin.online_shopping_shaojin.db.dao;

import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingOrder;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingShardingOrder;

public interface OnlineShoppingOrderDao {
    int insertOrder(OnlineShoppingOrder order);
    int insertOrderSharding(OnlineShoppingShardingOrder order);

    int update(OnlineShoppingOrder order);

    OnlineShoppingOrder queryOrder(Long orderId);
    OnlineShoppingOrder queryOrderSharding(Long orderId, Long userID);//定的规则是userId决定去哪个库，orderId决定去哪个表

    int deleteOder(Long orderId);

    OnlineShoppingOrder queryOrderByNumber(String orderNumber);//然后去orderdaoimpl里加这个方法

}
