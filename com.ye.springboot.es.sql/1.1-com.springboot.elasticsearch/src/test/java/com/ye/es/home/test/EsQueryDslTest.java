package com.ye.es.home.test;

import com.ye.es.my.factory.EsClientFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

/**
 * Created by zjx on 2017/10/24 0024.
 */
public class EsQueryDslTest {

    //普通查询单字段
    @Test
    public void matchAllQueryTest(){
        QueryBuilder qb = QueryBuilders.matchQuery(
                "project",
                "insure.pay.web"
        );

        SearchResponse response = EsClientFactory.getTransportClient().prepareSearch("hb.test.bt.p05-2017-10-24", "hb.test.bt.p06-2017-10-24")
                .setTypes("05", "06")
                .setQuery(qb).get();
        System.out.println(qb);
        System.out.println(response);
    }
    //多字段查询
    @Test
    public void multiMatchQueryTest(){
        QueryBuilder qb = QueryBuilders.multiMatchQuery(
                "insure.app.api",
                "datetime","message"
        );

        SearchResponse response = EsClientFactory.getTransportClient().prepareSearch("hb.test.bt.p05-2017-10-24", "hb.test.bt.p06-2017-10-24")
                .setTypes("05", "06")
                .setQuery(qb).get();
        System.out.println(qb);
        System.out.println(response);
    }

    @Test
    public void queryStringQueryTest(){
        QueryBuilder qb = QueryBuilders.queryStringQuery(
                "+|-2017-10-24 -c5ee1af95c771d4fcce3c3ec6d77be83"
        );

        SearchResponse response = EsClientFactory.getTransportClient().prepareSearch("hb.test.bt.p05-2017-10-24", "hb.test.bt.p06-2017-10-24")
                .setTypes("05", "06")
                .setQuery(qb).get();
        System.out.println(qb);
        System.out.println(response);
    }

}
