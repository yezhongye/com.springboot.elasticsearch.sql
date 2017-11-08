package com.ye.es.home.test;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zjx on 2017/11/6 0006.
 */
public class EsHikariCPTest {

    private static HikariDataSource ds;
    static{

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/db.order?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("root");
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

//        config.setJdbcUrl("jdbc:elasticsearch://127.0.0.1:9300/elasticsearch");
//        config.setDriverClassName("com.mysql.jdbc.Driver");
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
    }

    public static Connection getConn() throws SQLException {
        return ds.getConnection();
    }

//    @Test
    public void myHikariCPTest() throws SQLException {
       Connection con = EsHikariCPTest.getConn();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM tb_order");
//        PreparedStatement ps = con.prepareStatement("SELECT * FROM indexanimation");
        ResultSet rs = ps.executeQuery();
        System.out.println(resultSetToJson(rs));
        con.close();
    }
    @Test
    public void myDruidTest() throws Exception {
        Properties properties = new Properties();
        properties.put("url","jdbc:mysql://localhost:3306/db.order?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        properties.put("username","root");
        properties.put("password","root");
        properties.put("driverClassName","com.mysql.jdbc.Driver");
        //通过DruidDataSourceFactory获取DataSource实例
//        DataSource dds= DruidDataSourceFactory.createDataSource(properties);
        DruidDataSource dds = (DruidDataSource)DruidDataSourceFactory.createDataSource(properties);
        Connection conn=dds.getConnection();
        PreparedStatement st=conn.prepareStatement("SELECT * FROM tb_order limit 10");
        ResultSet result=st.executeQuery();
        System.out.println(resultSetToJson(result));
        result.close();
        st.close();
        conn.close();
    }
    private static String resultSetToJson(ResultSet rs) throws SQLException,JSONException
    {
        // json数组
        JSONArray array = new JSONArray();
        // 获取列数
        ResultSetMetaData metaData = rs.getMetaData();
        System.out.println(JSON.toJSON(metaData));
        int columnCount = metaData.getColumnCount();
        // 遍历ResultSet中的每条数据
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();
            // 遍历每一列
            for (int i = 1; i <= columnCount; i++) {
                String columnName =metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
            array.add(jsonObj);
        }
        return array.toString();
    }
}
