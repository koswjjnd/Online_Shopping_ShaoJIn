package com.online_shopping_shaojin.online_shopping_shaojin.db.dao;

import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingOrder;

public interface OnlineShoppingOrderDao {
    int insertOrder(OnlineShoppingOrder order);

    int update(OnlineShoppingOrder order);

    OnlineShoppingOrder queryOrder(Long orderId);

    int deleteOder(Long orderId);

    OnlineShoppingOrder queryOrderByNumber(String orderNumber);//然后去orderdaoimpl里加这个方法

}
