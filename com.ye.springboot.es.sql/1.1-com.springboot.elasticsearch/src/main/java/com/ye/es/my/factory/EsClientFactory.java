package com.ye.es.my.factory;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by zjx on 2017/10/11 0011.
 */
public class EsClientFactory {

//    public static final String CLUSTER_NAME = "hb-eslog"; //实例名称
//    private static final String IP = "192.168.3.220";
//    private static final int PORT = 9300;  //端口
    //===本地配置
//    public static final String CLUSTER_NAME = "elasticsearch"; //实例名称
//    private static final String IP = "localhost";
//    private static final int PORT = 9300;  //端口
    //====乔的配置
    public static final String CLUSTER_NAME = "elasticsearch"; //实例名称
    private static final String IP = "192.168.1.121";
    private static final int PORT = 9300;  //端口
    //1.设置集群名称：默认是elasticsearch，并设置client.transport.sniff为true，使客户端嗅探整个集群状态，把集群中的其他机器IP加入到客户端
    private static Settings settings = Settings.builder()
            .put("cluster.name", CLUSTER_NAME)
            .put("client.transport.sniff", true)
            .build();
    //创建私有对象
    private static TransportClient client;
    static {
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP), PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    //取得实例
    public static  TransportClient getTransportClient() {
        return client;
    }


}
