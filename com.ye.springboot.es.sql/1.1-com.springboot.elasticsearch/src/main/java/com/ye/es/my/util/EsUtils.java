package com.ye.es.my.util;

import com.ye.es.my.factory.EsQueryBuildersFactory;
import com.ye.es.my.factory.EsClientFactory;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.Script;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by zjx on 2017/10/12 0012.
 */
public class EsUtils {

    //查询
    public GetResponse getIndexResponse(String indexname, String type, String id){
        TransportClient client= EsClientFactory.getTransportClient();
        GetResponse response = client.prepareGet(indexname,type,id).get();
        return response;
    }
    //新增-1
    /**
     * 创建索引
     * @param indexname
     * @param type
     * @param jsondata
     * @return
     */
    public IndexResponse createIndexResponse(String indexname, String type,String id, String jsondata){
        TransportClient client= EsClientFactory.getTransportClient();
        IndexResponse response = null;
        if(StringUtils.isBlank(id)){
            response = client.prepareIndex(indexname, type)
                    .setSource(jsondata, XContentType.JSON)
                    .get();
        }else{
            response = client.prepareIndex(indexname, type,id)
                    .setSource(jsondata, XContentType.JSON)
                    .get();
        }
        return response;
    }
    //新增-2
    //jsonBuilder方式创建索引
    public IndexResponse createIndexResponseByBuilder(String indexname, String type,String id, Map<String,Object> mapData) throws IOException {
        TransportClient client= EsClientFactory.getTransportClient();
        IndexResponse response = null;
        if(StringUtils.isBlank(id)){
            response = client.prepareIndex(indexname, type)
                    .setSource(EsQueryBuildersFactory.createJsonBuilder(mapData))
                    .get();
        }else{
            response = client.prepareIndex(indexname, type,id)
                    .setSource(EsQueryBuildersFactory.createJsonBuilder(mapData))
                    .get();
        }
        return response;
    }
    //更新-1
    public UpdateResponse updateIndexResponseByScript(String indexname, String type, String id, String jsondata) throws ExecutionException, InterruptedException {
        TransportClient client= EsClientFactory.getTransportClient();
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexname);
        updateRequest.type(type);
        updateRequest.id(id);
        updateRequest.script(new Script("ctx._source.message = \"" + jsondata + "\"")).script(new Script("ctx._source.ega = \"" + jsondata + "\""));
        UpdateResponse updateResponse =  client.update(updateRequest).get();
        return updateResponse;
    }
    //更新-2
    public UpdateResponse updateIndexResponseByMerging(String indexname, String type, String id, Map<String,Object> mapData) throws ExecutionException, InterruptedException, IOException {
        TransportClient client= EsClientFactory.getTransportClient();
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexname);
        updateRequest.type(type);
        updateRequest.id(id);
        updateRequest.doc(EsQueryBuildersFactory.createJsonBuilder(mapData));
        UpdateResponse updateResponse =  client.update(updateRequest).get();
        return updateResponse;
    }
    //删除
    public DeleteResponse deleteIndexResponse(String indexname, String type, String id){
        TransportClient client= EsClientFactory.getTransportClient();
        DeleteResponse response = client.prepareDelete(indexname, type, id)
                .get();
        return  response;
    }

}
