package com.example.RPC;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg.toString().equals("test connection")){
                ctx.channel().writeAndFlush("test back\r\n");
                return;
            }
        Response response = JSON.parseObject(msg.toString(), Response.class);
        DefaultFuture.receive(response);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    }
}
