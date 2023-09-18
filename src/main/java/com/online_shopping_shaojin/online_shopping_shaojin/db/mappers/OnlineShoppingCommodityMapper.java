//蓝色鸟头生成了增删改查的方法，具体怎么实现就是看红色鸟头里
//mapper层是用来直接连接底层sql和dao层
package com.online_shopping_shaojin.online_shopping_shaojin.db.mappers;

import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;

import java.util.List;

public interface OnlineShoppingCommodityMapper {
    int deleteByPrimaryKey(Long commodityId);

    int insert(OnlineShoppingCommodity record);

    int insertSelective(OnlineShoppingCommodity record);

    OnlineShoppingCommodity selectByPrimaryKey(Long commodityId);

    int updateByPrimaryKeySelective(OnlineShoppingCommodity record);

    int updateByPrimaryKey(OnlineShoppingCommodity record);

    List<OnlineShoppingCommodity> listCommoditiesByUserId(Long userId);
}