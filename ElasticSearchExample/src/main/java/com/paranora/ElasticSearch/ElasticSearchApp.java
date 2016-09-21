package com.paranora.ElasticSearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by yangqun on 2016/09/10.
 */
public class ElasticSearchApp {



    public static TransportClient generateClient(String ip,int port,String clusterName) throws  Exception{
        Settings settings = Settings.settingsBuilder()
//                .put("client.transport.sniff", true)
//                .put("node.client", true)
//                .put("node.data",false).
//                .put("node.name","es.cluster.a.node.6")
//                .put("network.host", "10.4.254.30:9300")
                .put("cluster.name", clusterName)
//                .put("network.server","false")
//                .put("client.type","transport")
                .build();

        TransportClient client = TransportClient.builder().settings(settings)
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
        return client;
    }
}
