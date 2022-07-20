package com.wu.qqclient.service;

import java.util.HashMap;

/**
 * 线程池,管理线程
 */

public class ManageClientConnectServiceThread {
    //创建一个HashMap的集合来存储管理线程
    private static HashMap<String, ClientConnectService> hm = new HashMap();

    /**
     * 向集合中添加线程
     * @param userId 用户名
     * @param clientConnectService 线程
     */
    public static void addClientConnectServiceThread(String userId, ClientConnectService clientConnectService) {
        hm.put(userId, clientConnectService);
    }

    /**
     * 以用户名返回对应线程
     * @param userId 用户名
     * @return 线程
     */
    public static ClientConnectService getClientConnectServiceThread(String userId) {
        return hm.get(userId);
    }

    /**
     * 删除一个线程
     * @param userId 用户名
     */
    public static void removeClientServiceThread(String userId) {
        hm.remove(userId);
    }
}
