package com.example.RPC;

import com.alibaba.fastjson.JSON;
import com.example.RPC.User.User;
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
            ch = client.connect(host, port).sync();
        } catch (InterruptedException e) {
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
        ch.channel().writeAndFlush(JSON.toJSONString(request));
        ch.channel().writeAndFlush("\r\n");
        DefaultFuture df=new DefaultFuture(request);
        return df.get();
    }
}
