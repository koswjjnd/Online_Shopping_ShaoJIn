package com.online_shopping_shaojin.online_shopping_shaojin.db.dao.impl;
//创建 Commodity, User, Order 对应的 DAO 具体实现，调用 mybatisMapper , 实现 DAO 层对 user, order, commodity 的 CRUD
import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingCommodityDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.mappers.OnlineShoppingCommodityMapper;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
@Repository
public class OnlineShoppingCommodityDaoImpl implements OnlineShoppingCommodityDao {
    @Resource
    OnlineShoppingCommodityMapper mapper;
    @Override
    public int deleteCommodityById(Long commodityId) {

        return mapper.deleteByPrimaryKey(commodityId);
    }

    @Override
    public int insertCommodity(OnlineShoppingCommodity record) {

        return mapper.insert(record);
    }

    @Override
    public OnlineShoppingCommodity queryCommodityById(Long commodityId) {
        return mapper.selectByPrimaryKey(commodityId);
    }

    @Override
    public int updateCommodity(OnlineShoppingCommodity record) {

        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public List<OnlineShoppingCommodity> listCommoditiesByUserId(Long userId) {
        return mapper.listCommoditiesByUserId(userId);
    }
}
