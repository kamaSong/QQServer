package com.ycj.QQServer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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

    //返回在线用户列表
    public static   String getOnlineUser()
    {
        //遍历线程集合 ，获取key
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while(iterator.hasNext())
        {
            onlineUserList +=  iterator.next().toString()+" ";
        }


   return onlineUserList;
    }
}
