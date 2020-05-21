package org.hyzs.utils;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @program: esIndex
 * @description:
 * @author: hux
 * @create: 2020-04-01 12:05
 **/
public class ElasticSearchUtils {
    private static class SingletonHolder {
        private static final TransportClient transportClient = new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", "es601")
                .build()).addTransportAddress(new TransportAddress(new InetSocketAddress("172.16.119.32", 9300)));
    }

    public static final TransportClient getInstance() {
        return SingletonHolder.transportClient;
    }

    public static void writeBulk(JSONObject json) {
        SingletonHolder.transportClient.prepareBulk().add(SingletonHolder.transportClient.prepareIndex("judicialdocument", "xsyspj")
                .setSource(json, XContentType.JSON));
    }
    public static void excuteBulk(){
        SingletonHolder.transportClient.prepareBulk().execute().actionGet();

    }

    public static void write(JSONObject json,String docid) throws UnknownHostException {

//        ElasticSearchConnector elasticSearchConnector=new ElasticSearchConnector();
        SingletonHolder.transportClient.prepareIndex("judicialdocument", "xsyspj")
                .setTimeout(TimeValue.timeValueMillis(60 * 24))
                .setId(docid)
                .setSource(json, XContentType.JSON)
                .execute(new ActionListener<IndexResponse>() {
                    @Override
                    public void onResponse(IndexResponse indexResponse) {
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("执行失败"+docid);
                        StringUtils.saveFailedInfo("faildocid.txt",docid);
                    }
                });

//        String _index=response.getIndex().toString();
//        System.out.println(_index);
    }

}
