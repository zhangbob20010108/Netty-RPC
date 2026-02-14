# Netty/RPC

## 简介
这个项目实现了基本的Netty客户端和服务端的通信功能，使用了Springboot进行整合，并且通过Zookeeper对服务端进行登记管理。

## 具体实现的功能 
- **自定义心跳机制**
- **Zookeeper登记管理存活服务器**
- **反射动态获取调用方法**
- **RPC远程接口调用**
- **Springboot整合服务器配置**
- **TCP客户端长连接请求**
- **Zookeeper监听器跟踪服务端节点状态**
- **负载均衡连接**

## 注意事项
该项目使用JDK17版本，安装Zookeeper3.4.6版本

## 测试项目运行
- 在RPC项目下启动RpcApplication类启动服务端，client项目下启动TCPClient的main方法发送消息。
- 
**服务端启动成功**
<img width="1787" height="960" alt="NettyServer" src="https://github.com/user-attachments/assets/fa8339bf-9a3f-4bc4-aaab-d868e50bf52c" />

**客户端成功发送和接收**
<img width="1800" height="945" alt="client" src="https://github.com/user-attachments/assets/bbd702a4-bed7-4a8d-85c2-cc85958aeaa3" />

**成功启动Zookeeper服务器**
<img width="1102" height="618" alt="zookeeper-server" src="https://github.com/user-attachments/assets/de5de17f-5bf0-4774-8aae-2bc908bae6f7" />




