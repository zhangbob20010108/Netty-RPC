package com.example.RPC;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg.toString().equals("test back")){
            return;
        }

        try {
            ClientRequest request= JSON.parseObject(msg.toString(), ClientRequest.class);
            Response response=new Response();
            response.setId(request.getId());
            response.setResult("Sucess");
            ctx.channel().writeAndFlush(JSON.toJSONString(response));
            ctx.channel().writeAndFlush("\r\n");
        }catch (Exception e){
                e.printStackTrace();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event=(IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)){
                System.out.println("read Idle");
                ctx.channel().close();
            }else if (event.state().equals(IdleState.WRITER_IDLE)){
                System.out.println("write Idle");
            }else if (event.state().equals(IdleState.ALL_IDLE)){
                System.out.println("all Idle");
                ctx.channel().writeAndFlush("test connection\r\n");
            }
        }
    }
}
