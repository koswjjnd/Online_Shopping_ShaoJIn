package com.online_shopping_shaojin.online_shopping_shaojin.service;

import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SearchService {
    @Resource
    EsService esService;
    public List<OnlineShoppingCommodity> searchCommodityByEs(String keyword){
        return esService.searchCommodities(keyword,0,20);
    }


}
