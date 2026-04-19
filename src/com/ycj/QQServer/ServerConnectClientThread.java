package com.ycj.QQServer;

import com.ycj.QQcommon.Message;
import com.ycj.QQcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

//服务器端连接客户端的线程类
public class ServerConnectClientThread extends Thread{
    private Socket socket;

    //与哪个用户通信
    private String userId;

    public ServerConnectClientThread(Socket socket,String userId){
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
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
                else if (message.getMessageType().equals(MessageType.MESSAGE_CLIENT_EXIT))
                {

                    //关闭socket
                    System.out.println(userId+"下线");  //将对应uid的线程从集合中移除
                    ManagerClientThreads.removeClientThread(message.getSender());//发送的客户端
                    socket.close();
                    //退出循环
                    break;

                }else if (message.getMessageType().equals(MessageType.MESSAGE_COMM_MES))
                {
                    //接受消息  分别获取接收者对应线程
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManagerClientThreads.
                            getClientThread(message.getReceiver()).socket.getOutputStream());
                    objectOutputStream.writeObject(message);//转发 消息

                }
                else if (message.getMessageType().equals(MessageType.MESSAGE_GROUP_MES))
                {
                    //群发消息，
                    //获取所有客户端线程，遍历，除了发送者，都发送
                    HashMap<String, ServerConnectClientThread> clientThreads = ManagerClientThreads.getClientThreads();
                    Iterator<String> iterator = clientThreads.keySet().iterator();
                    while (iterator.hasNext()) {
                        //取出在线用户
                        String onlineUser = iterator.next().toString();
                        //非本人直接发
                        if(!onlineUser.equals(message.getSender()))
                        {
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientThreads.get(onlineUser).
                                    socket.getOutputStream());
                            objectOutputStream.writeObject(message);
                        }
                        
                    }
                    

                }
                //文件
                else if (message.getMessageType().equals(MessageType.MESSAGE_FILE_MES))
                {
                    //根据接收者id，获取对应的线程，获取socket，获取输出流，写入数据
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManagerClientThreads.
                            getClientThread(message.getReceiver()).socket.getOutputStream());
                    objectOutputStream.writeObject(message);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
