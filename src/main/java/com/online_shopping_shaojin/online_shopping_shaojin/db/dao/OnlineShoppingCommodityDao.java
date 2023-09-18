package com.online_shopping_shaojin.online_shopping_shaojin.db.dao;
//dao层是抽象层，是逻辑层调用mapper中间过渡的地方，成功隔离了两者

import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;

import java.util.List;

public interface OnlineShoppingCommodityDao {
    int deleteCommodityById(Long commodityId);
    int insertCommodity(OnlineShoppingCommodity record);
    OnlineShoppingCommodity queryCommodityById(Long commodityId);
    int updateCommodity(OnlineShoppingCommodity record);

    List<OnlineShoppingCommodity> listCommoditiesByUserId(Long userId);
}
