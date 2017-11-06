package com.ye.es.home.test;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        DruidDataSource dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        Connection connection = dds.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM hb.dev.bt.p01-2017-11-06");
        ResultSet resultSet = ps.executeQuery();
        List<String> result = new ArrayList<String>();
        while (resultSet.next()) {
            result.add(resultSet.getString("host") + "," + resultSet.getString("project") + "," + resultSet.getString("message"));
//            System.out.println(resultSet.getString(1));
//            result.add(resultSet.getString(i++));
        }

        ps.close();
        connection.close();
        dds.close();
//
        for(int i=0;i<result.size();i++){
            System.out.println(result.get(i));
        }


    }
}
