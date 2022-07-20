package com.wu.qqcommon;

public interface MessageType {
    //不同的值代表不同的消息类型
    String MESSAGE_LOGIN_SUCCED = "1";//登录成功
    String MESSAGE_LOGIN_FAIL = "2";//登录失败
    String MESSAGE_COMM_MES = "3";//常规消息(私聊)
    String MESSAGE_GET_ONLINE_FRIEND = "4";//拉取在线用户列表(客户端)
    String MESSAGE_RET_ONLINE_FRIEND = "5";//拉取在线用户列表(服务器端)
    String MESSAGE_CLIENT_EXIT = "6";//退出系统
    String MESSAGE_NOT_FOUND_USER = "7";//未找到用户
    String MESSAGE_NOT_ONLINE_USER = "8";//未在线用户
    String MESSAGE_GROUP_CHAT_MES = "9";//发送群聊消息
    String MESSAGE_FILE_MES = "10";//发送文件
    String MESSAGE_COMM_MES_TOALL = "11";//服务器推送
    String MESSAGE_OFFLINE_MES = "12";//离线消息

}
