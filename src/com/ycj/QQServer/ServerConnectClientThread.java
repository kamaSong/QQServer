package com.ycj.QQServer;

import com.ycj.QQcommon.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

//服务器端连接客户端的线程类
public class ServerConnectClientThread extends Thread{
    private Socket socket;

    //与哪个用户通信
    private String userId;

    public ServerConnectClientThread(Socket socket,String userId){
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {
        while(true){
            //与客户端通信  读数据写数据
            System.out.println("与客户端"+userId+"通信");
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
