package com.example.client;

import io.netty.channel.ChannelFuture;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ChannelManager {
   public static CopyOnWriteArrayList<ChannelFuture>channelFutures=new CopyOnWriteArrayList<>();
   static AtomicInteger index=new AtomicInteger(0);
   static Set<String> nodes=new HashSet<>();
   public static ChannelFuture rotate(){
       ChannelFuture f;
       if (channelFutures.isEmpty()){
           return null;
       }
        if (channelFutures.size()<=index.get()){
            f=channelFutures.get(0);
            index.set(0);
        }else {
            f=channelFutures.get(index.get());
        }
        index.getAndIncrement();
        System.out.println("index"+index);
        if (!f.channel().isActive()){
            channelFutures.remove(f);
            return rotate();
        }
        return f;
   }
}
