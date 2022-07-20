package com.wu.qqclient.service;

import com.wu.qqcommon.Message;
import com.wu.qqcommon.MessageType;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 消息发送相关功能模块
 */

public class MessageClientService {

    /**
     * 私聊
     * @param content 内容
     * @param senderId 发送用户
     * @param getterId 接收用户
     */
    public void privateChat(String content,String senderId,String getterId){
        Message message = new Message();//可以用构造器
        message.setSender(senderId);//发送用户
        message.setGetter(getterId);//接收用户
        message.setContent(content);//内容
        message.setSendTime(getTime());//当前时间
        System.out.println(senderId+"对"+getterId+"说:"+content);
        message.setMesType(MessageType.MESSAGE_COMM_MES);//消息类型为私聊
        try {
            //通过用户名得到线程池中对应线程,再通过该线程得到的socket对象get输出流
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServiceThread.
                    getClientConnectServiceThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群聊
     * @param content 内容
     * @param senderId 发送用户
     */
    public void groupChat(String content,String senderId){
        Message message = new Message();
        message.setSender(senderId);//发送用户
        message.setContent(content);//接收用户
        message.setSendTime(getTime());//当前时间
        System.out.println(senderId+"对所有人说:"+content);
        message.setMesType(MessageType.MESSAGE_GROUP_CHAT_MES);
        try {
            //通过用户名得到线程池中对应线程,再通过该线程得到的socket对象get输出流
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServiceThread.
                    getClientConnectServiceThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 以特定格式得到当前时间并返回
     * @return 当前时间
     */
    private String getTime(){
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(ldt);
    }
}
