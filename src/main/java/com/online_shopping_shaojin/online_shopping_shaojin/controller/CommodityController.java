package com.online_shopping_shaojin.online_shopping_shaojin.controller;



import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingCommodityDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
@Controller
public class CommodityController {

    @Resource
    OnlineShoppingCommodityDao commodityDao;

    @GetMapping("/addItem")
    public String addItem() {
        return "add_commodity";
    }
    @PostMapping("/addItemAction")//通过add_commodity这个html里的<form th:action="@{/addItemAction}" method="post">这一行知道添加商品后要跳到这里
    public String addItemAction(@RequestParam("commodityId") long commodityId,
                                @RequestParam("commodityName") String commodityName,
                                @RequestParam("commodityDesc") String commodityDesc,
                                @RequestParam("price") int price,
                                @RequestParam("availableStock") int availableStock,
                                @RequestParam("creatorUserId") long creatorUserId,
                                Map<String, Object> resultMap) {
            OnlineShoppingCommodity newCommodity = OnlineShoppingCommodity.builder()
            .commodityId(commodityId)
            .commodityName(commodityName)
            .commodityDesc(commodityDesc)
            .price(price)
            .availableStock(availableStock)
            .creatorUserId(creatorUserId)
            .totalStock(availableStock)
            .lockStock(0)
            .build();
        commodityDao.insertCommodity(newCommodity);
        resultMap.put("Item", newCommodity);//在html里通过th:去寻找这里的String，找到了“Item”是我要去交互的
        return "add_commodity_success";
    }
    @GetMapping("/listItems/{sellerId}")
    public String listItem(@PathVariable("sellerId") long sellerId,
                           Map<String, Object> resultMap){
        List<OnlineShoppingCommodity> onlineShoppingCommodities = commodityDao.listCommoditiesByUserId(sellerId);
        resultMap.put("itemList", onlineShoppingCommodities);
        return "list_items";
    }
    @RequestMapping("/item/{commodityId}")
    public String getItem(@PathVariable("commodityId") long commodityId,
                          Map<String, Object> resultMap){
        OnlineShoppingCommodity commodity = commodityDao.queryCommodityById(commodityId);
        resultMap.put("commodity", commodity);
        return "item_detail";

    }


}
