package com.online_shopping_shaojin.online_shopping_shaojin.controller;



import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingCommodityDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import com.online_shopping_shaojin.online_shopping_shaojin.service.EsService;
import com.online_shopping_shaojin.online_shopping_shaojin.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Controller
@Slf4j
public class CommodityController {

    @Resource
    OnlineShoppingCommodityDao commodityDao;
    @Resource
    SearchService searchService;
    @Resource
    EsService esService;

    @GetMapping("/addItem")
    public String addItem() {
        return "add_commodity";
    }

    @RequestMapping("/staticItem/{commodityId}")
    public String staticItemPage(
            @PathVariable("commodityId") long commodityId
    ) {
        return "item_detail_" + commodityId;
    }
    @PostMapping("/addItemAction")
//通过add_commodity这个html里的<form th:action="@{/addItemAction}" method="post">这一行知道添加商品后要跳到这里
    public String addItemAction(@RequestParam("commodityId") long commodityId,
                                @RequestParam("commodityName") String commodityName,
                                @RequestParam("commodityDesc") String commodityDesc,
                                @RequestParam("price") int price,
                                @RequestParam("availableStock") int availableStock,
                                @RequestParam("creatorUserId") long creatorUserId,
                                Map<String, Object> resultMap) throws IOException {
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
        esService.addCommodityToEs(newCommodity);
        resultMap.put("Item", newCommodity);//在html里通过th:去寻找这里的String，找到了“Item”是我要去交互的
        return "add_commodity_success";
    }

    @GetMapping("/listItems/{sellerId}")
    public String listItem(@PathVariable("sellerId") long sellerId,
                           Map<String, Object> resultMap) throws BlockException {
        try (Entry entry = SphU.entry("listItemsRule", EntryType.IN, 1,
                sellerId)) {//要用CommodityControllerFlow定义的那个规则了
            List<OnlineShoppingCommodity> onlineShoppingCommodities = commodityDao.listCommoditiesByUserId(sellerId);
            resultMap.put("itemList", onlineShoppingCommodities);
            return "list_items";
        } catch (BlockException e) {
            log.error("ListItems got throttled" + e.toString());
            return "wait";
        }
    }

    @RequestMapping("/item/{commodityId}")
    public String getItem(@PathVariable("commodityId") long commodityId,
                          Map<String, Object> resultMap) {
        OnlineShoppingCommodity commodity = commodityDao.queryCommodityById(commodityId);
        resultMap.put("commodity", commodity);
        return "item_detail";

    }

    @RequestMapping("/searchAction")
    public String queryItem(@RequestParam("keyWord") String keyword,
                            Map<String, Object> resultMap) {
        List<OnlineShoppingCommodity> onlineShoppingCommodities = searchService.searchCommodityByEs(keyword);
        resultMap.put("itemList", onlineShoppingCommodities);
        return "search_items_by_keyword";
    }

    @PostConstruct//紧接着构造函数去执行
    public void CommodityControllerFlow() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
// Define resource
        rule.setResource("listItemsRule");//只是定义这个规则名字，想在listitem里增加限流这个规则
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//Define QPS count
        rule.setCount(1);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);

    }
}
