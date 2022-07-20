package com.wu.qqclient.service;

import com.wu.qqclient.utils.StreamUtils;
import com.wu.qqclient.utils.Utility;
import com.wu.qqcommon.FileMessage;
import com.wu.qqcommon.Message;
import com.wu.qqcommon.MessageType;


import java.io.*;
import java.util.Date;


/**
 * 完成读取本地文件并发送给服务器的功能
 */

public class FileClientService {

    private static final String ABSPATH = "E:\\java\\java_qqproject\\qqclientfile";

    /**
     * 读取本地文件转换为二进制数组并发送数据报
     *
     * @param src      文件源地址
     * @param dest     文件目标地址
     * @param senderId 发送用户
     * @param getterId 接收用户
     */
    public void sendFile(String src, String dest, String senderId, String getterId) {
        Message message = new Message();
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSendTime(new Date().toString());
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        FileInputStream fileInputStream = null;
        File file = new File(src);
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                byte[] bytes = StreamUtils.streamToByteArray(fileInputStream);
                int fileLen = bytes.length;
                message.setFileMessage(new FileMessage(bytes, fileLen, src, dest));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                //通过用户名得到线程池中对应线程,再通过该线程得到的socket对象get输出流
                ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServiceThread.
                        getClientConnectServiceThread(senderId).getSocket().getOutputStream());
                oos.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("该文件不存在");
        }
    }

    public void sendFile(String src, String senderId, String getterId) {
        Message message = new Message();
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSendTime(new Date().toString());
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        FileInputStream fileInputStream = null;
        File file = new File(src);
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                byte[] bytes = StreamUtils.streamToByteArray(fileInputStream);
                int fileLen = bytes.length;
                message.setFileMessage(new FileMessage(bytes, fileLen, src));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                //通过用户名得到线程池中对应线程,再通过该线程得到的socket对象get输出流
                ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServiceThread.
                        getClientConnectServiceThread(senderId).getSocket().getOutputStream());
                oos.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("该文件不存在");
        }
    }

    static void readFile(String getterId, FileMessage fileMessage) {
        String dest = getFilePath(getterId, fileMessage);

        fileMessage.setDest(dest);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileMessage.getDest());
            fileOutputStream.write(fileMessage.getFileBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static String getFilePath(String getterId, FileMessage fileMessage) {
        File file = new File(ABSPATH + "\\" + getterId);
        if (!file.exists()) {
            file.mkdir();
        }
        File file1 = new File(fileMessage.getSrc().trim());
        String fileName = file1.getName();
        return ABSPATH + "\\" + getterId + "\\" + fileName;
    }
}
