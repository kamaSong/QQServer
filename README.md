# iChat-Server — 即时通讯服务端

课程设计项目 | Java + ServerSocket + 多线程

## 项目简介

基于 ServerSocket 的即时通讯系统服务端，监听客户端连接，处理登录验证、消息转发、在线用户管理等功能。

**开发周期**：2022.11 - 2022.12

## 技术栈

- Java
- ServerSocket（TCP）
- 多线程
- 对象序列化

## 核心功能

- **用户验证**：预设用户池，验证登录请求
- **并发处理**：每客户端创建独立线程处理通信
- **消息转发**：私聊转发至指定用户，群发遍历所有在线用户
- **在线用户管理**：HashMap 管理客户端线程
- **文件透传**：接收文件消息，原样转发至目标客户端
- **系统日志推送**：向客户端推送系统通知日志

## 项目结构

| 文件 | 作用 |
|------|------|
| QQframe.java | 启动类，创建 Server 实例 |
| Server.java | 服务端主类，监听端口，验证登录 |
| ServerConnectClientThread.java | 通信线程，处理客户端消息 |
| ManagerClientThreads.java | 线程管理（HashMap） |

## 并发设计

- 主线程循环 `accept()` 阻塞等待客户端连接
- 每个客户端创建独立 `ServerConnectClientThread` 线程
- 线程存入 `HashMap<String, Thread>` 统一管理
- 消息转发时根据用户 ID 获取对应线程

## 消息类型

| 类型 | 说明 |
|------|------|
| `MESSAGE_LOGIN_SUCCEED` | 登录成功 |
| `MESSAGE_LOGIN_FAIL` | 登录失败 |
| `MESSAGE_COMM_MES` | 私聊消息 |
| `MESSAGE_GET_ONLINE_FRIEND` | 请求在线列表 |
| `MESSAGE_RET_ONLINE_FRIEND` | 返回在线列表 |
| `MESSAGE_CLIENT_EXIT` | 客户端退出 |
| `MESSAGE_GROUP_MES` | 群发消息 |
| `MESSAGE_FILE_MES` | 文件传输 |

