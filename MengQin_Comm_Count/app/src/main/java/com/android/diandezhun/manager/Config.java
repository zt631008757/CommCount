package com.android.diandezhun.manager;

/**
 * Created by zt on 2018/8/13.
 */

public class Config {

    public static String SERVER_HOST = "";      //域名地址
    public static String SERVER_HOST_Compute = "";      //图像识别地址

    // 环境
    public static final int IS_A = 0;
    public static final int IS_R = 1;

    public static int curVersion = IS_A;

    static {
        switch (curVersion) {
            case IS_A:
                SERVER_HOST = "https://diandezhun.mengqin.vip/";
                SERVER_HOST_Compute = "https://compute.mengqin.vip/";
                break;
            case IS_R:
                SERVER_HOST = "";

        }
    }
}
