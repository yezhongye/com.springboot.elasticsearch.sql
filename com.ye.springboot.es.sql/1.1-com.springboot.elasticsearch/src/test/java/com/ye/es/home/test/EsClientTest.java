package com.ye.es.home.test;

import com.ye.es.my.factory.EsClientFactory;
import com.ye.es.my.util.EsUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by zjx on 2017/10/11 0011.
 */
public class EsClientTest {
    Logger log = LogManager.getLogger(EsClientTest.class);

    public TransportClient createTTransportClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "hb-eslog").build();
        TransportClient client = new PreBuiltTransportClient(settings)
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.220"), 9200))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.220"), 9300));
        return client;
    }


    public void close(TransportClient client){
        client.close();
    }

    @Test
    public void clientTest() throws IOException {

        TransportClient client = this.createTTransportClient();
        System.out.println(client.toString());
        GetResponse response = client.prepareGet("messages-2017.09.29","logs","AV7Mp61Tl8e-24lpjaDR").get();
        System.out.println(response);
        client.close();

    }
//=================================
    //查询
    @Test
    public void clientFactoryQueryTest(){
        String indexname = "twitter";
        String type ="tweet" ;
        String id = "AV8Vh-aITPcQXziRlbCm";
//        String id = "AV7Mp5ZoonBXshdrNfRG";
        EsUtils esUtil = new EsUtils();
        GetResponse response = esUtil.getIndexResponse(indexname, type, id);
        log.info("111:" + response);
        System.out.println(response);
    }
    //创建索引-json方式
    @Test
    public void clientFactoryCreateTest(){
        String indexname = "twitter";
        String type ="tweet" ;
        String id = "";
        String json = "{" +
                "\"user\":\"kimchy12345_02\"," +
                "\"postDate\":\""+getFormatDate(new Date())+"\"," +
                "\"message\":\"trying out Elasticsearch 12345\"" +
                "}";
        EsUtils esUtil = new EsUtils();
        IndexResponse response = esUtil.createIndexResponse(indexname,type ,id,json );
        System.out.println(response);
    }
    //创建索引-builder方式
    @Test
    public void clientFactoryCreateTest2() throws IOException {
        String indexname = "indexmedicines";
        String type ="typemedicines" ;
        String id = "";
       Map<String,Object> mapData = new HashMap<String, Object>();
        mapData.put("id",1);
        mapData.put("name","一贴灵");
        mapData.put("funciton","治疗：跌打损伤，活血化瘀");
        EsUtils esUtil = new EsUtils();
        IndexResponse response = esUtil.createIndexResponseByBuilder(indexname, type, id, mapData);
        System.out.println(response);
    }
    @Test
    public void clientFactoryUpdateTest() throws ExecutionException, InterruptedException {
        String indexname = "twitter";
        String type ="tweet" ;
        String id = "AV8Vh-aITPcQXziRlbCm";
        String json = "trying out Elasticsearch 12345 update by script";
        EsUtils esUtil = new EsUtils();
        UpdateResponse response = esUtil.updateIndexResponseByScript(indexname, type, id, json);
        System.out.println(response);
    }
    @Test
    public void clientFactoryUpdateTest2() throws ExecutionException, InterruptedException, IOException {
        String indexname = "indexanimation";
        String type ="typeanimation" ;
        String id = "AV8p5F-xswldUW4R8n1o";
        Map<String,Object> mapData = new HashMap<String, Object>();
        mapData.put("id",3);
        mapData.put("name","jingangxia01");
        mapData.put("funciton","英文版一共6集");
        EsUtils esUtil = new EsUtils();
        UpdateResponse response = esUtil.updateIndexResponseByMerging(indexname, type, id, mapData);
        System.out.println(response);
    }
    @Test
    public void clientFactoryDeleteTest(){
        String indexname = "name";
        String type ="" ;
        String id = "";
        EsUtils esUtil = new EsUtils();
        DeleteResponse response = esUtil.deleteIndexResponse(indexname, type, id);
        System.out.println(response);
    }


    public String getFormatDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateJson = format.format(date);
        return dateJson;
    }
}
