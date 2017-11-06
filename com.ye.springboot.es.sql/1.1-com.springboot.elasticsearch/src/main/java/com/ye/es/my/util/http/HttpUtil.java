package com.ye.es.my.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zd.yao on 2017/4/12.
 */
public class HttpUtil {
    public static String sendPost(String url, Map<String, String> params) {
        List<NameValuePair> urlParameters = getNameValuePairs(params);
        String response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("User-Agent", "Mozilla/5.0");
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters, StandardCharsets.UTF_8));
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    public static String sendGet(String url,Map<String, String> params){
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>(params.size());
        for (String key : params.keySet()) {
            urlParameters.add(new BasicNameValuePair(key, params.get(key)));
        }
        //
        String response =null;
        try{
            URI uri = new URIBuilder(url).addParameters(urlParameters).build();
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader( "User-Agent" , "Mozilla/5.0");
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity,StandardCharsets.UTF_8);
            httpClient.close();
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
        return response.toString();
    }

    public static String sendPostXML(String url, String xml) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("User-Agent", "Mozilla/5.0");
        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
        httpPost.setEntity(entity);
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String response = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
        httpClient.close();
        return response;
    }
    private static List<NameValuePair> getNameValuePairs(Map<String, String> params) {
        if (params == null) {
            return new ArrayList<NameValuePair>();
        }
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>(params.size());
        for (String key : params.keySet()) {
            urlParameters.add(new BasicNameValuePair(key, params.get(key)));
        }
        return urlParameters;
    }
    /**
     * https双向签名认证，用于支付申请退款
     *
     * */
    public static String payHttps(String url,String xml,String path) throws Exception {
        //商户id
        String MCH_ID = "1468261502";
        //指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //读取本机存放的PKCS12证书文件
//        FileInputStream instream = new FileInputStream(new File(path));
        InputStream instream = null;
        try {
            instream =  new Resource().getInputStream();
            //指定PKCS12的密码(商户ID)
            keyStore.load(instream, MCH_ID.toCharArray());
        } catch (Exception e){
            throw new Exception(e+"证书文件读取异常");
        }finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, MCH_ID.toCharArray()).build();
        //指定TLS版本
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,new String[] { "TLSv1" },null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        //设置httpclient的SSLSocketFactory
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();


        try {
            HttpPost httpPost = new HttpPost(url); // 设置响应头信息
            httpPost.addHeader("User-Agent", "Mozilla/5.0");
            HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
            httpPost.setEntity(entity);
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            try {
                HttpEntity httpEntity = httpResponse.getEntity();
                String response = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
                return response;
            } finally {
                httpResponse.close();
            }
        } finally {
            httpClient.close();
        }
    }
    static class Resource
    {
        public InputStream getInputStream()
        {
            return getClass().getResourceAsStream("/apiclient_cert.p12");
        }
    }
}

