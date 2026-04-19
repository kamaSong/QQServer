package com.ycj.QQServer;

import com.ycj.QQcommon.Message;
import com.ycj.QQcommon.MessageType;
import com.ycj.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class SendnewsALLservice implements Runnable{
    //
    @Override
    public void run() {
        //不加while循环，程序会直接退出，因为没有其他线程在运行
        while(true){

            System.out.println("请输入要广播的消息[exit可退出]");
            String news = Utility.readString(100);
            if("exit".equals(news)){
                break;
            }
            Message message = new Message();
            //设置消息类型
            message.setMessageType(MessageType.MESSAGE_GROUP_MES);
            message.setContent(news);
            message.setSender("服务端");
            message.setSendTime(new Date().toString());

            System.out.println("服务器推送消息给所有用户");

            //遍历ServerConnectClientThread线程的集合，获取每一个线程
            HashMap<String, ServerConnectClientThread> clientThreads = ManagerClientThreads.getClientThreads();
            Iterator<String> iterator = clientThreads.keySet().iterator();
            while (iterator.hasNext()) {
                String onlineUser = iterator.next();
                //获取每一个线程
                ServerConnectClientThread clientThread = clientThreads.get(onlineUser);//通过集合获取线程
                //发送消息
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientThread.getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }



        }
    }
}
