package org.hyzs.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @program: esIndex
 * @description:
 * @author: hux
 * @create: 2020-04-01 12:25
 **/
public class ElasticSearchConnector {
//    public static void main(String[] args) throws UnknownHostException {
//       ElasticSearchConnector elasticSearchConnector=new ElasticSearchConnector();
//       elasticSearchConnector.client();
//    }

    /** 实例化es客户端 */
    @SuppressWarnings(value = { "resource", "unchecked" })
    public TransportClient client() throws UnknownHostException {
      return  new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", "es601")
                .build()).addTransportAddress(new TransportAddress(new InetSocketAddress("172.16.119.32", 9300)));

    }
    public void dispose() throws UnknownHostException {
        client().close();
    }
}
