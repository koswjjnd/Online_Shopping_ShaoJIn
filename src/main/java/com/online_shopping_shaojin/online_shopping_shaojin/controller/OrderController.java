package com.online_shopping_shaojin.online_shopping_shaojin.controller;

import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingCommodityDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.dao.OnlineShoppingOrderDao;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingOrder;
import com.online_shopping_shaojin.online_shopping_shaojin.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Controller
public class OrderController {

    @Resource
    OnlineShoppingOrderDao orderDao;
    @Resource
    OrderService orderService;
    @Resource
    OnlineShoppingCommodityDao commodityDao;
    @GetMapping("/commodity/buy/{userId}/{commodityId}")//这个路径从item_detailhtml里去找<a th:href="'/commodity/buy/124/' + ${commodity.commodityId}" target="_blank"
    public String addOrder(@PathVariable("userId") Long userId,
                           @PathVariable("commodityId") Long commodityId,
                           Map<String, Object> resultMap) {
        OnlineShoppingOrder onlineShoppingOrder = orderService.processOrder(userId, commodityId);//新建一个orderservice的意义是我们之后要写一堆orderservice的processorder的方法
        if (onlineShoppingOrder!=null){
            resultMap.put("resultInfo", "create order successfully, "+"order num is "+onlineShoppingOrder.getOrderNo());
            resultMap.put("orderNo", onlineShoppingOrder.getOrderNo()); //这个order_result里有两个地方需要我这边输入，所以result_map加了两次
            return "order_result";
        }
        else{
            resultMap.put("resultInfo", "commodity is out of stock");
            return "order_result";
        }

    }
    @RequestMapping("/commodity/orderQuery/{orderNumber}")
    public String queryOrder(@PathVariable("orderNumber") String orderNumber,
                             Map<String, Object> resultMap){
        OnlineShoppingOrder order=orderService.queryOrder(orderNumber);
        OnlineShoppingCommodity commodity = commodityDao.queryCommodityById(order.getCommodityId());
        resultMap.put("order",order);
        resultMap.put("commodity", commodity);
        return "order_check";

    }
    @RequestMapping("/commodity/payOrder/{orderNumber}")
    public String payOrder(@PathVariable("orderNumber") String orderNumber){
        orderService.payOrder(orderNumber);
        return "redirect:/commodity/orderQuery/"+orderNumber;


    }



}