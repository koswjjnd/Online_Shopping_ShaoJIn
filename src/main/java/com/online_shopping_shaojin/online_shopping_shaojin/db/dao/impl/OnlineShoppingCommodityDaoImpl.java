package com.online_shopping_shaojin.online_shopping_shaojin.db.dao.impl;
//创建 Commodity, User, Order 对应的 DAO 具体实现，调用 mybatisMapper , 实现 DAO 层对 user, order, commodity 的 CRUD
import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingCommodityDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.mappers.OnlineShoppingCommodityMapper;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public int deductStock(Long commodityId) {
        return mapper.deductStock(commodityId);
    }

    @Override
    public int deductStockWithStoreProcedure(Long commodityId) {
        Map<String, Object> para=new HashMap<>();
        para.put("commodityId", commodityId);
        para.put("res",0);
        mapper.deductStockWithStoreProcedure(para);
        return (int) para.getOrDefault("res", 0);
    }

    @Override
    public int revertStock(Long commodityId) {
        return mapper.revertStock(commodityId);
    }

    @Override
    public List<OnlineShoppingCommodity> queryKeyword(String keyword) {
        return mapper.queryKeyword('%'+keyword+'%');
    }
}
