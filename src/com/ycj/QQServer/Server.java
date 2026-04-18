package com.ycj.QQServer;

import com.ycj.QQcommon.Message;
import com.ycj.QQcommon.MessageType;
import com.ycj.QQcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

//服务器类,监听客户端的请求，处理客户端的请求
public class Server {
    private ServerSocket serverSocket=null;
    //创建集合用户记录
    //处理并发集合  ConcurrentHashmap 线程安全
    private static HashMap<String,User> validUsers = new HashMap<>();

    //初始化
    static {
        validUsers.put("zzw",new User("zzw","123456"));
        validUsers.put("ycj",new User("ycj","371425"));
        validUsers.put("zx",new User("zx","1566857"));
    }
    //可以通过线程判断，实现单点登录
    //验证用户
    private   boolean checkUser(String userId,String password)
    {
        User user = validUsers.get(userId);
        if(user==null)//用户不存在
        {
            return false;
        }
        else
        {
            if(!password.equals(user.getPassword()))
            {
                return false;
            }
        }
        return true;
    }


    public Server()  {
        System.out.println("服务器在监听9999端口...");
        try {
            serverSocket = new ServerSocket(9999);

            //来自客户端的socket有很多,要循环监听
            while(true){
                Socket socket = serverSocket.accept();//监听生成socket
                //读取客户端发送的数据
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                //此时读取user对象
                User user = (User) objectInputStream.readObject();
                //先写死验证
                //公用创建message对象，加输出流
                Message message = new Message();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                //读取成功，发送信息登陆成功
                if(checkUser(user.getUserId(),user.getPassword()))
                {
                    //合法用户  返回message对象
                    message.setMessageType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //回复 输出
                    objectOutputStream.writeObject(message);
                    //成功创建一个线程和客户端保持通信，线程池有socket
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket,user.getUserId());
                    serverConnectClientThread.start();
                    //把线程放入到集合中进行管理
                    ManagerClientThreads.addClientThread(user.getUserId(), serverConnectClientThread);
                }
                else
                {
                    System.out.println("用户"+user.getUserId()+"登录失败");
                    //不合法用户  返回message对象
                    message.setMessageType(MessageType.MESSAGE_LOGIN_FAIL);
                    //回复 输出
                    objectOutputStream.writeObject(message);
                    //退出
                    socket.close();

                }}
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }



}
