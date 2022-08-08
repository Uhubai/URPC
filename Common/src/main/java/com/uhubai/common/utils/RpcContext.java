package com.uhubai.common.utils;


public class RpcContext {
    private static String localIp;


    public static void setLocalIp(String localIp) {
        RpcContext.localIp = localIp;
    }


    public static String getLocalIp() {
        return localIp;
    }
}
