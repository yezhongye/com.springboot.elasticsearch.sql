package com.ye.es.home.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Created by zjx on 2017/11/15 0015.
 */
public class EsRestTest {

    @Test
    public void restClientGetTest() throws IOException {
        RestClient restClient = RestClient.builder(
//            new HttpHost("localhost", 9201, "http"),
                new HttpHost("localhost", 9200, "http")).build();
        Map<String, String> params = Collections.singletonMap("pretty", "true");
        Response response = restClient.performRequest("GET", "/",params);
//        RequestLine requestLine = response.getRequestLine();
        HttpHost host = response.getHost();
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        restClient.close();
    }
    @Test
    public void restClientPutTest() throws IOException {
        RestClient restClient = RestClient.builder(
//            new HttpHost("localhost", 9201, "http"),
                new HttpHost("localhost", 9200, "http")).build();
        Map<String, String> params = Collections.emptyMap();
        String jsonString = "{" +
                "\"user\":\"kimchynew01\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest("PUT", "/posts/doc/1", params, entity);
        System.out.println(response.getStatusLine().getStatusCode());
        restClient.close();
    }
    @Test
    public void restClientPostTest() throws IOException {
        RestClient restClient = RestClient.builder(
//            new HttpHost("localhost", 9201, "http"),
                new HttpHost("localhost", 9200, "http")).build();
        Map<String, String> params = Collections.emptyMap();
        String jsonString = "{" +
                "\"user\":\"kimchynew01\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest("POST", "/posts/doc/1", params, entity);
        System.out.println(response.getStatusLine());
        restClient.close();
    }
    @Test
    public void restClientDeleteTest() throws IOException {
        RestClient restClient = RestClient.builder(
//            new HttpHost("localhost", 9201, "http"),
                new HttpHost("localhost", 9200, "http")).build();
        Response response = restClient.performRequest("DELETE", "/posts");
        System.out.println(response.getStatusLine());
        restClient.close();
    }

}
