package com.example.client;

import com.example.client.User.User;
import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TCPClient {
    static String host="localhost";
    static int port=8080;
    static Bootstrap client;
    static EventLoopGroup worker;
    static ChannelFuture ch;

    static {
        client=new Bootstrap();
        worker=new NioEventLoopGroup();
        client.group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()))
                                .addLast(new StringDecoder())
                                .addLast(new SimpleClientHandler())
                                .addLast(new StringEncoder());
                    }
                });
        try {
            CuratorFramework zk= ZookeeperFactory.create();
            zk.getChildren().usingWatcher(new ServerWatcher()).forPath("/netty");

            List<String> nodes = zk.getChildren().forPath("/netty");
            for (String node:nodes){
                System.out.println(node);
                String[] ip = node.split("#");
                String host=ip[0];
                String port=ip[1];
                ChannelManager.nodes.add(host+"#"+port);
            }
            for (String node:ChannelManager.nodes){
                String[] ip = node.split("#");
                String host=ip[0];
                int port=Integer.parseInt(ip[1]);
                ChannelFuture f = TCPClient.client.connect(host, port).sync();
                ChannelManager.channelFutures.add(f);
            }
            //ch = client.connect(host, port).sync();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public static void main(String[] args) {
       /* ClientRequest request=new ClientRequest();
        request.setContent("hello TCP");
        Response response = TCPClient.send(request);
        System.out.println("first try");
        System.out.println(response.getResult());

        ClientRequest request1=new ClientRequest();
        request1.setContent("TCP");
        response = TCPClient.send(request1);
        System.out.println("second try");
        System.out.println(response.getResult()); */

        User user=new User();
        user.setId(1);
        user.setName("BOB");
        ClientRequest request=new ClientRequest();
        request.setCommand("com.example.RPC.User.UserController.save");
        request.setContent(user);
        Response response = TCPClient.send(request);
        System.out.println(response.getResult());
        System.out.println(response.getStatus());

        ClientRequest request1=new ClientRequest();
        request1.setCommand("com.example.RPC.User.UserController.save");
        request1.setContent(user);
        Response response1 = TCPClient.send(request1);
        System.out.println(response1.getResult());
        System.out.println(response1.getStatus());
    }
    public static Response send(ClientRequest request){
        ch=ChannelManager.rotate();
        ch.channel().writeAndFlush(JSON.toJSONString(request));
        ch.channel().writeAndFlush("\r\n");
        DefaultFuture df=new DefaultFuture(request);
        return df.get();
    }
}
