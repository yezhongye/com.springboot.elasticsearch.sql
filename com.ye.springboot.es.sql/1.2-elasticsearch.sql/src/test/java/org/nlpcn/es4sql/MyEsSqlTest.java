package org.nlpcn.es4sql;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.Token;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.*;
import org.nlpcn.es4sql.domain.Select;
import org.nlpcn.es4sql.parse.ElasticSqlExprParser;
import org.nlpcn.es4sql.parse.SqlParser;
import org.nlpcn.es4sql.query.AggregationQueryAction;
import org.nlpcn.es4sql.query.DefaultQueryAction;
import org.nlpcn.es4sql.query.SqlElasticSearchRequestBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by zjx on 2017/11/1 0001.
 */
public class MyEsSqlTest {

    @org.junit.Test
    public void test1(){

    }

    @org.junit.Test
    public void testMy() throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.220"), 9300));


        String sql = "select * from indexanimation where id ='3'";
        //其中采用的是阿里的druid框架，
        //其中ElasticLexer和ElasticSqlExprParser都是对druid中的MySql的进行了扩展
        SQLExprParser parser = new ElasticSqlExprParser(sql);
        SQLExpr expr = parser.expr();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql);
        }
        SQLQueryExpr queryExpr=(SQLQueryExpr) expr;
        //通过抽象语法树，封装成自定义的Select，包含了select、from、where group、limit等
        Select select = new SqlParser().parseSelect(queryExpr);

        AggregationQueryAction action;
        DefaultQueryAction queryAction = null;
        if (select.isAgg) {
            //包含计算的的排序分组的
            //request.setSearchType(SearchType.DEFAULT);
            action= new AggregationQueryAction(client, select);
        } else {
            //封装成自己的Select对象
            queryAction = new DefaultQueryAction(client, select);
        }
        // 把属性封装在SearchRequestBuilder(client.prepareSearch()获取的即ES中获取的方法)对象中
        // 然后装饰了一下SearchRequestBuilder为自定义的SqlElasticSearchRequestBuilder
        SqlElasticSearchRequestBuilder requestBuilder = queryAction.explain();
        //之后就是对ES的操作
        SearchResponse response=(SearchResponse) requestBuilder.get();
        SearchHit[] hists = response.getHits().getHits();
        System.out.println(hists.length);
        for(SearchHit hit:hists){
            System.out.println(hit.getSourceAsString());
        }
    }
}
