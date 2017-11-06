package com.ye.es.home.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.ye.es.my.util.http.HttpUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zjx on 2017/10/30 0030.
 */
public class EsHttpClientSqlTest {

    public static final String CLUSTER_NAME = "elasticsearch"; //实例名称
    private static final String IP = "127.0.0.1";
    private static final int PORT = 9200;  //端口
//    http://localhost:9200/_sql
    @Test
    public void esHttpClientTest() throws UnsupportedEncodingException {
        String url = "http://"+IP +":"+ PORT +"/_sql";
        Map<String,String> requestMap = new HashMap<String, String>();
        String sql = "select id,name,funciton as fu from indexanimation,indexmedicines where id between 1 and 2 order by id";
        requestMap.put("sql",sql);
        String response = HttpUtil.sendGet(url,requestMap);
        System.out.println(response);
    }

}
