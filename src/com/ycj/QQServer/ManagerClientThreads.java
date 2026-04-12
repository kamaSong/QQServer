package com.ycj.QQServer;

import java.util.HashMap;

// 管理客户端线程的类
public class ManagerClientThreads {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static void addClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    //根据id获取客户端线程
    public static ServerConnectClientThread getClientThread(String userId) {
        return hm.get(userId);
    }
}
