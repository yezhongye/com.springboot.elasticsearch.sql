package org.nlpcn.es4sql;


import com.alibaba.druid.pool.DruidDataSource;

import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;
import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by allwefantasy on 8/26/16.
 */
public class JDBCTests {
    @Test
    public void testJDBC() throws Exception {
        Properties properties = new Properties();
        properties.put("url", "jdbc:elasticsearch://127.0.0.1:9300/elasticsearch");
        DruidDataSource dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        Connection connection = dds.getConnection();
//        PreparedStatement ps = connection.prepareStatement("SELECT  gender,lastname,age from  " + TestsConstants.TEST_INDEX + " where lastname='Heath'");
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM indexanimation");
        ResultSet resultSet = ps.executeQuery();
        List<String> result = new ArrayList<String>();
        int i=0;
        while (resultSet.next()) {
            result.add(resultSet.getInt("id") + "," + resultSet.getString("name") + "," + resultSet.getString("funciton"));
//            System.out.println(resultSet.getString(1));
//            result.add(resultSet.getString(i++));
        }

        ps.close();
        connection.close();
        dds.close();
//
        System.out.println(result.get(0));
//        Assert.assertTrue(true);
//        Assert.assertTrue(result.get(0).equals("5,一贴灵,治疗：跌打损伤，活血化瘀"));
    }

}


