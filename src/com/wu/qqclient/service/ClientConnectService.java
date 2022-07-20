package com.wu.qqclient.service;

import com.wu.qqcommon.Message;
import com.wu.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 客户端socket通信线程,监听接收并根据消息类型做出反馈
 */

public class ClientConnectService extends Thread {
    private Socket socket;
    //private ObjectInputStream ois = null;

    public ClientConnectService(Socket socket) {
        this.socket = socket;
    }

    //线程
    @Override
    public void run() {
        while (true) {
            try {
                //创建输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //读取对象
                Message message = (Message) ois.readObject();

                //拉取用户列表
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    System.out.println("\n当前在线用户列表:");
                    String[] onlineUsers = message.getContent().split(" ");
                    for (String s : onlineUsers) {
                        System.out.println("用户: " + s);
                    }
                    //私聊消息
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    System.out.println("\n"+message.getSender() + "发来消息:" + message.getContent());
                    System.out.println("============================================");
                    //群聊消息
                } else if (message.getMesType().equals(MessageType.MESSAGE_GROUP_CHAT_MES)) {
                    System.out.println("\n"+message.getSender() + "发来消息:" + message.getContent());
                    System.out.println("============================================");
                    //用户不存在
                } else if (message.getMesType().equals(MessageType.MESSAGE_NOT_ONLINE_USER)) {
                    System.out.println("\n该用户未在线,用户上线时将通知");

                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    System.out.println(message.getSender() + "发来文件");
                    FileClientService.readFile(message.getGetter(),message.getFileMessage());

                } else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES_TOALL)){
                    System.out.println("\n"+message.getSender() + "发来消息:" + message.getContent());

                } else if (message.getMesType().equals(MessageType.MESSAGE_NOT_FOUND_USER)) {
                    System.out.println("\n该用户不存在");

                } else if (message.getMesType().equals(MessageType.MESSAGE_OFFLINE_MES)){
                    if (message.getFileMessage() != null){
                        System.out.println("\n" +message.getSender() + "发来文件,地址为:"+FileClientService.getFilePath
                                (message.getGetter(),message.getFileMessage()));
                        FileClientService.readFile(message.getGetter(),message.getFileMessage());
                    }else {
                        System.out.println("\n" + message.getSender() + "发来消息:" + message.getContent());
                    }
                }
//                else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
//                    System.out.println("用户" + message.getGetter() + "退出");
//                    ManageClientConnectServiceThread.removeClientServiceThread(message.getGetter());
//                    ois.close();
//                    socket.close();
//                    return;
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return this.socket;
    }
}
