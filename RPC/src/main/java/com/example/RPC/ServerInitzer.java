package com.example.RPC;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

@Component
public class ServerInitzer implements ApplicationListener<ContextRefreshedEvent> {
    public static final String PATH="/netty";
    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        bootstrap.group(boss,worker);
        bootstrap.option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,false)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()))
                                .addLast(new StringDecoder())
                                .addLast(new IdleStateHandler(60,45,20, TimeUnit.SECONDS))
                                .addLast(new ServerHandler())
                                .addLast(new StringEncoder());
                    }
                });
        try {
            int port=8080;
            int port1=8081;
            int port2=8082;
            ChannelFuture f = bootstrap.bind(port).sync();
            ChannelFuture f1 = bootstrap.bind(port1).sync();
            ChannelFuture f2 = bootstrap.bind(port2).sync();

            CuratorFramework client=ZookeeperFactory.create();
            InetAddress ip4=InetAddress.getLocalHost();

            client.create()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(PATH+"/"+ip4.getHostAddress()+"#"+port+"#");

            client.create()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(PATH + "/" + ip4.getHostAddress() + "#" + port1 + "#");

            client.create()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(PATH + "/" + ip4.getHostAddress() + "#" + port2 + "#");

            f.channel().closeFuture().sync();
            f1.channel().closeFuture().sync();
            f2.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
            this.start();
    }
}
