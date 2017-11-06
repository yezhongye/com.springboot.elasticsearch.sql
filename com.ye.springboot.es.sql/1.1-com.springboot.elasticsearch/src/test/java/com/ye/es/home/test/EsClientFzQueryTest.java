package com.ye.es.home.test;

import com.ye.es.my.factory.EsClientFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

/**
 * Created by zjx on 2017/10/13 0013.
 */
public class EsClientFzQueryTest {
    Logger log = LogManager.getLogger(EsClientFzQueryTest.class);

    //普通搜索
    @Test
    public void searchTest(){
        SearchResponse response = EsClientFactory.getTransportClient().prepareSearch("indexmedicine", "indexanimation")
                .setTypes("typemedicine", "typeanimation")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery(QueryBuilders.termQuery("name", "感"))
//                .setQuery(QueryBuilders.termQuery("name", "冒"))
//                .setQuery(QueryBuilders.termQuery("name", "灵"))
//                .setQuery(QueryBuilders.termQuery("name", "颗"))
                .setQuery(QueryBuilders.termQuery("name", "粒"))
//                .setQuery(QueryBuilders.queryStringQuery("4"))
//                .setQuery(QueryBuilders.termQuery("name", "jingangxia01"))
                .setPostFilter(QueryBuilders.rangeQuery("id").from(1).to(4))
                .setFrom(0).setSize(60).setExplain(true)
                .get();
        System.out.println(response);

//        SearchResponse response2 = EsClientFactory.getTransportClient().prepareSearch("twitter", "twitter01").get();
//        log.info("=="+response2);
    }
    //查询大量数据,可以用来分页
    @Test
    public void scrollSearchTest(){
        QueryBuilder qb = QueryBuilders.queryStringQuery("冒");

        SearchResponse scrollResp = EsClientFactory.getTransportClient().prepareSearch("indexmedicine", "indexanimation")
//                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .addSort("id", SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(2).get(); //max of 100 hits will be returned for each scroll
        //Scroll until no hits are returned
        long nbHits = 1;

        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                //Handle the hit...
                if(nbHits == 1){
                    log.info("hit = "+hit.getSource());
                }

            }
            nbHits++;
            scrollResp = EsClientFactory.getTransportClient().prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
//            System.out.println(scrollResp);
        } while(scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.
    }

    //整合查询多个client正和数据查询
    @Test
    public void multiSearchTest(){
        SearchRequestBuilder srb1 = EsClientFactory.getTransportClient()
                .prepareSearch().setQuery(QueryBuilders.queryStringQuery("4")).setSize(2);
        SearchRequestBuilder srb2 = EsClientFactory.getTransportClient()
                .prepareSearch().setQuery(QueryBuilders.matchQuery("name", "jingangxia01")).setSize(1);

        MultiSearchResponse sr = EsClientFactory.getTransportClient().prepareMultiSearch()
                .add(srb1)
                .add(srb2)
                .get();

        long nbHits = 0;
        for(MultiSearchResponse.Item item : sr.getResponses()){
            SearchResponse response = item.getResponse();
            nbHits += response.getHits().getTotalHits();
            System.out.println(response);
        }
        log.info(nbHits);
    }
    //聚合查询
    @Test
    public void aggregationsSearchTest(){
//        SearchResponse sr = EsClientFactory.getTransportClient().prepareSearch("indexmedicine", "indexanimation")
//                .setQuery(QueryBuilders.matchAllQuery())
//                .addAggregation(
//                        AggregationBuilders.terms("group1").field("id")
//                )
//                .addAggregation(
//                        AggregationBuilders.dateHistogram("agg2")
//                                .field("name")
//                                .dateHistogramInterval(DateHistogramInterval.DAY)
//                )
//                .get();
//        // Get your facet results
//        InternalStats agg1 = sr.getAggregations().get("group1");
//        InternalValueCount agg2 = sr.getAggregations().get("agg2");
//        System.out.println(sr);
//        System.out.println(agg1.getAvg());
//        System.out.println(agg2.getValue());
    }
}
