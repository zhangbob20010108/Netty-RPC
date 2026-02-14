package com.example.RPC;

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
import io.netty.util.AttributeKey;

public class NettyClient {

    public static void main(String[] args) {
        String host="localhost";
        int port=8080;
        Bootstrap client=new Bootstrap();
        EventLoopGroup worker=new NioEventLoopGroup();
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
            ChannelFuture ch = client.connect(host, port).sync();
            ch.channel().writeAndFlush("hello i am client");
            ch.channel().writeAndFlush("\r\n");
            ch.channel().closeFuture().sync();
            Object result = ch.channel().attr(AttributeKey.valueOf("sssss")).get();
            System.out.println("server result is "+result.toString());
        } catch (InterruptedException e) {
           e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }

    }
}
