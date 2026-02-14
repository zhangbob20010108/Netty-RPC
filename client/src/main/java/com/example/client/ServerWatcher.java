package com.example.client;

import io.netty.channel.ChannelFuture;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import java.util.HashSet;
import java.util.List;

public class ServerWatcher implements CuratorWatcher {
    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
        CuratorFramework client = ZookeeperFactory.create();
        String path = watchedEvent.getPath();
        System.out.println("change: "+path);
        client.getChildren()
                .usingWatcher(this)
                .forPath(path);
        List<String> nodes = client.getChildren().forPath(path);
        ChannelManager.nodes.clear();
        for (String node:nodes){
            System.out.println("node"+node);
            String[] ip = node.split("#");
            String host=ip[0];
            String port=ip[1];
            ChannelManager.nodes.add(host+"#"+port);
        }
        for (ChannelFuture future : ChannelManager.channelFutures) {
            future.channel().close();
        }
        ChannelManager.channelFutures.clear();

        for (String node:ChannelManager.nodes){
            String[] ip = node.split("#");
            String host=ip[0];
            int port=Integer.parseInt(ip[1]);
            ChannelFuture f = TCPClient.client.connect(host, port).sync();
            ChannelManager.channelFutures.add(f);
        }

    }
}
