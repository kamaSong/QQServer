package com.ycj.QQServer;

import com.ycj.QQcommon.Message;
import com.ycj.QQcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
                //除了第一次通信读取user，其他循环和中读取message
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();

                //读取 发来的信息  类型做判断
                if(message.getMessageType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND))
                {
                    //发送客户端要显示   100  1000   yng
                    System.out.println(message.getSender()+"要在线用户列表");
                    String onlineUser = ManagerClientThreads.getOnlineUser();

                    //构建message
                    Message message1 = new Message();
                    message1.setMessageType(MessageType.MESSAGE_RET_ONLINE_FRIEND);

                    message1.setContent(onlineUser);
                    message1.setReceiver(message.getSender()); //将原来发过来的客户端变为接收者


                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(message1);




                }
                else {
                    //
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
