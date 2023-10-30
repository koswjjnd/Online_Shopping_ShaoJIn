package com.online_shopping_shaojin.online_shopping_shaojin.service;

import com.alibaba.fastjson.JSON;
import com.online_shopping_shaojin.online_shopping_shaojin.db.po.OnlineShoppingCommodity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EsService {//提供elastic search的增删改查
    @Resource
    RestHighLevelClient restHighLevelClient;
    public int addCommodityToEs(OnlineShoppingCommodity commodity) throws IOException {
        try {
            String indexName = "commodity";
            boolean indexExist = restHighLevelClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
            if (!indexExist) {
                //create commodity Index
                XContentBuilder builder = XContentFactory.jsonBuilder();
                builder.startObject()
                        .startObject("dynamic_templates")
                        .startObject("strings")
                        .field("match_mapping_type", "string")
                        .startObject("mapping")
                        .field("type", "text")
                        .field("analyzer", "ik_smart")
                        .endObject()
                        .endObject()
                        .endObject()
                        .endObject();//动态template（指插入一条信息的时候他才定义每个attribute）
                CreateIndexRequest request = new CreateIndexRequest(indexName);
                request.source(builder);
                CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
                if (!response.isAcknowledged()) {
                    log.error("Failed to create ES index: commodity");
                    return 500;
                }

            }
            // Create Document into Commodity Index
            String data = JSON.toJSONString(commodity);
            IndexRequest request = new
                    IndexRequest("commodity").source(data, XContentType.JSON);
            IndexResponse response = restHighLevelClient.index(request,
                    RequestOptions.DEFAULT);
            log.info("addCommodityToES commodity:{} result:{}",
                    data, response);
            return response.status().getStatus();
        }catch (IOException e){
            throw new RuntimeException(e);
        }


    }

    public List<OnlineShoppingCommodity> searchCommodities(String keyword, int from, int size) {

        try {
            //构建查询请求，指定查询的索引库
            SearchRequest searchRequest = new SearchRequest("commodity");
            //创建查询条件构造器 SearchSourceBuilder
            SearchSourceBuilder searchSourceBuilder = new
                    SearchSourceBuilder();
            MultiMatchQueryBuilder queryBuilder =
                    QueryBuilders.multiMatchQuery(keyword, "commodityName",
                            "commodityDesc");
            //指定查询条件
            searchSourceBuilder.query(queryBuilder);
            /*
             * 指定分页查询信息
             * 从哪里开始查
             */
            searchSourceBuilder.from(from);
            //每次查询的数量
            searchSourceBuilder.size(size);
            /*
             * 设置排序规则
             * 按照销量排序
             */
            searchSourceBuilder.sort("price", SortOrder.DESC);
            searchRequest.source(searchSourceBuilder);
            //查询获取查询结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest,
                    RequestOptions.DEFAULT);
            System.out.println(JSON.toJSONString(searchResponse));
            //获取命中对象
            SearchHits searchHits = searchResponse.getHits();
            long totalNum = searchHits.getTotalHits().value;
            log.info("serarch 总记录数： {}", totalNum);
            List<OnlineShoppingCommodity> onlineShoppingCommodities =
                    new ArrayList<>();
            //获取命中的 hits 数据,搜索结果数据
            SearchHit[] hits = searchHits.getHits();
            for (SearchHit searchHit : hits) {
                //获取 json 字符串格式的数据
                String sourceAsString = searchHit.getSourceAsString();
                OnlineShoppingCommodity onlineShoppingCommodity =
                        JSON.parseObject(sourceAsString, OnlineShoppingCommodity.class);

                onlineShoppingCommodities.add(onlineShoppingCommodity);
            }
            log.info("serarch result {}",
                    JSON.toJSONString(onlineShoppingCommodities));
            return onlineShoppingCommodities;
        } catch (Exception e) {
            log.error("SearchService searchCommodities error", e);
            return null;
        }
    }
}

