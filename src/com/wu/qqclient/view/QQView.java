package com.wu.qqclient.view;

import com.wu.qqclient.service.FileClientService;
import com.wu.qqclient.service.MessageClientService;
import com.wu.qqclient.service.UserClientService;
import com.wu.qqclient.utils.Utility;

/**
 * 客户端的显示界面
 */
public class QQView {
    private boolean loop = true;//控制主菜单中循环
    private String key = "";
    private UserClientService userClientService = new UserClientService();//负责登陆,拉取用户列表,退出功能
    private MessageClientService messageClientService = new MessageClientService();//消息传输相关功能实现
    private FileClientService fileClientService = new FileClientService();

    //客户端主函数
    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("退出系统");
    }

    /**
     * 主菜单
     */
    private void mainMenu() {

        while (loop) {

            System.out.println("=============欢迎登录网络通信系统=============");
            System.out.println("\t\t\t 1 登录系统");
            System.out.println("\t\t\t 9 退出系统");
            System.out.print("请输入你的选择:");
            key = Utility.readString(1);

            switch (key) {
                //用户登录
                case "1":
                    System.out.print("请输入用户名: ");
                    String userId = Utility.readString(50);
                    System.out.print("请输入密  码: ");
                    String pwd = Utility.readString(20);
                    //检测用户名和密码
                    if (this.userClientService.checkUser(userId, pwd)) {
                        System.out.println("欢迎" + userId + "用户登录成功");
                        //二级菜单
                        while (loop) {
                            System.out.println("\n=============网络通信系统二级菜单(用户:" + userId + ")=============\"");
                            System.out.println("\t\t\t 1 显示在线用户列表");
                            System.out.println("\t\t\t 2 群发消息");
                            System.out.println("\t\t\t 3 私聊消息");
                            System.out.println("\t\t\t 4 发送文件");
                            System.out.println("\t\t\t 9 退出系统");
                            System.out.print("请输入你的选择: ");
                            key = Utility.readString(1);
                            switch (key) {
                                //拉取在线用户列表
                                case "1":
                                    userClientService.onlineFriendList();
                                    break;
                                //群发消息
                                case "2":
                                    System.out.print("输入群聊消息:");
                                    String info = Utility.readString(50);
                                    messageClientService.groupChat(info, userId);
                                    break;
                                //发送私聊消息
                                case "3":
                                    System.out.print("输入想要私聊的用户名(在线): ");
                                    String getterId = Utility.readString(50);
                                    System.out.print("请输入内容");
                                    String content = Utility.readString(100);
                                    messageClientService.privateChat(content, userId, getterId);
                                    break;
                                //发送文件
                                case "4":
                                    System.out.print("输入想要发送文件的用户名:");
                                    String fileGetterId = Utility.readString(50);
                                    System.out.print("输入想要传输的文件地址");
                                    String src = Utility.readString(50);
                                    fileClientService.sendFile(src,userId,fileGetterId);
                                    break;
                                //退出系统
                                case "9":
                                    userClientService.exitQQClient();
                                    break;
                            }
                        }
                    } else {
                        System.out.println("=============登  录  失  败=============");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }
        }
    }


}


