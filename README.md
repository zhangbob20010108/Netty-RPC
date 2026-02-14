# Netty/RPC

## 简介
这个项目实现了基本的Netty客户端和服务端的通信功能，使用了Springboot进行整合，并且通过Zookeeper对服务端进行登记管理。

## 具体实现的功能 

自定义心跳机制
Zookeeper登记管理存活服务器
反射动态获取调用方法
RPC远程接口调用
Springboot整合服务器配置
TCP客户端长连接请求
Zookeeper监听器跟踪服务端节点状态
负载均衡连接

## 注意事项
该项目使用JDK17版本，安装Zookeeper3.4.6版本

## 测试项目运行
在RPC项目下启动RpcApplication类启动服务端，client项目下启动TCPClient的main方法发送消息。




