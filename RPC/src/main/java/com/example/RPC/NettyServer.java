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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

public class NettyServer {
    public static final String PATH="/netty";
    public static void main(String[] args) {
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
                                        .addLast(new SimpleServerHandler())
                                        .addLast(new StringEncoder());
                    }
                });
        try {
            ChannelFuture f = bootstrap.bind(8080).sync();

            CuratorFramework client=ZookeeperFactory.create();
            InetAddress ip4=InetAddress.getLocalHost();
            client.create().withMode(CreateMode.EPHEMERAL).forPath(PATH+"/"+ip4.getHostAddress());
            f.channel().closeFuture().sync();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
