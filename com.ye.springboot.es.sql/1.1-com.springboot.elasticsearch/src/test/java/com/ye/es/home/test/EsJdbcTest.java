package com.ye.es.home.test;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.druid.pool.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by zjx on 2017/11/6 0006.
 */
public class EsJdbcTest {
    @Test
    public void testJDBC() throws Exception {
        Properties properties = new Properties();
        properties.put("url", "jdbc:elasticsearch://192.168.3.220:9300/hb-eslog");
//        properties.put("url", "jdbc:elasticsearch://127.0.0.1:9300/elasticsearch");
        ElasticSearchDruidDataSource dds = (ElasticSearchDruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        ElasticSearchDruidPooledConnection connection = (ElasticSearchDruidPooledConnection)dds.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM hb.dev.bt.p01-2017-11-06");
//        ElasticSearchDruidPooledPreparedStatement ps = (ElasticSearchDruidPooledPreparedStatement)connection.prepareStatement("SELECT * FROM indexanimation,indexmedicines");
        ElasticSearchResultSet resultSet = (ElasticSearchResultSet)ps.executeQuery();
        System.out.println(resultSetToJson(resultSet));

        ps.close();
        connection.close();
        dds.close();
//



    }
    private static String resultSetToJson(ElasticSearchResultSet rs) throws SQLException,JSONException
    {
        // json数组
        JSONArray array = new JSONArray();
        // 获取列数
        List<String> metaData = rs.getHeaders();
        int columnCount = metaData.size();
        // 遍历ResultSet中的每条数据
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();
            // 遍历每一列
            for (int i = 0; i < columnCount; i++) {
                String columnName =metaData.get(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
            array.add(jsonObj);
        }
        return array.toString();
    }
}
