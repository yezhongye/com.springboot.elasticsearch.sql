package com.ye.es.my.factory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zjx on 2017/10/16 0016.
 */
public class EsQueryBuildersFactory {
    Logger log = LogManager.getLogger(EsQueryBuildersFactory.class);

    //根据传入的参数，构建相应的JsonBuilder
    public static XContentBuilder createJsonBuilder(Map<String,Object> mapData) throws IOException {
        XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject();

            Iterator<Map.Entry<String,Object>> iter = mapData.entrySet().iterator();
            while (iter.hasNext()){
                Map.Entry<String,Object> entry = iter.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                contentBuilder.field(key,value);
            }
    return contentBuilder.endObject();
    }
}
