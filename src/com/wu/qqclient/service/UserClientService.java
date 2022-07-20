package com.wu.qqclient.service;

import com.wu.qqcommon.Message;
import com.wu.qqcommon.MessageType;
import com.wu.qqcommon.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 负责用户登陆,拉取在线用户列表,退出系统功能
 */
public class UserClientService {
    private User user = new User();
    private boolean flag = false;
    private Socket socket;

    /**
     * 检测用户名密码,成功登陆开启socket线程
     * @param userId 用户名
     * @param pwd 密码
     * @return T/F
     */
    public boolean checkUser(String userId, String pwd) {
        user.setUserId(userId);
        user.setPasswd(pwd);

        try {
            //以本机为服务器.端口为9999的socket对象
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            //得到输出流
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //将user传输给服务器
            oos.writeObject(user);
            //socket输入流
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //读取数据报
            Message ms = (Message)ois.readObject();
            //登录成功
            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCED)) {
                //创建一个线程对象
                ClientConnectService clientConnectService = new ClientConnectService(socket);
                //开启线程
                clientConnectService.start();
                //将线程加入线程池中,以userId(用户名)为键值,线程对象为Value
                ManageClientConnectServiceThread.addClientConnectServiceThread(userId, clientConnectService);
                //成功登陆,返回值设为true
                flag = true;
            } else {
                //登录失败,关闭socket
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回值
        return flag;
    }

    /**
     * 拉取在线用户列表
     */
    public void onlineFriendList() {
        Message message = new Message();//创建一个数据报对象message
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);//设置消息雷响
        message.setSender(user.getUserId());//设置发送用户信息

        try {
            //通过用户名得到线程池中对应线程,再通过该线程得到的socket对象get输出流
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServiceThread.
                    getClientConnectServiceThread(this.user.getUserId()).getSocket().getOutputStream());
            //写入数据
            oos.writeObject(message);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    /**
     * 退出客户端下线
     */
    public void exitQQClient() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);//消息类型
        message.setSender(user.getUserId());//发送者信息

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println("用户:"+user.getUserId()+"退出系统");
            //ManageClientConnectServiceThread.removeClientServiceThread(user.getUserId());
            System.exit(0);//退出
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
