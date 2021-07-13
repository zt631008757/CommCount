package com.android.diandezhun.manager;

import android.content.Context;
import android.graphics.Color;

import com.android.baselibrary.tool.SPUtil;
import com.android.baselibrary.util.Util;
import com.android.diandezhun.constant.SPConstants;
import com.umeng.commonsdk.UMConfigure;

public class ImageRecConfig {

    public static final String SP_CircleColor = "SP_CircleColor";               //圆点颜色，系统识别的点
    public static final String SP_CircleColor_Custom = "SP_CircleColor_Custom"; //圆点颜色，手动添加的点
    public static final String SP_CircleStyle = "SP_CircleStyle";       //圆点形状
    public static final String SP_Frame_BgColor = "SP_Frame_BgColor";   //矩形框背景颜色
    public static final String SP_Show_index = "SP_Show_index";   //显示/隐藏 标记

    public static int getFramePadding(Context context)
    {
        return Util.dip2px(context,10);
    }



    //获取颜色
    public static int getCircleColor(Context context) {
        int index = SPUtil.getIntValue(context, SP_CircleColor, 3);
        String color = "#9602FC04";
        switch (index) {
            case 0:
                color = "#80FF0F00";
                break;
            case 1:
                color = "#80FFBE00";
                break;
            case 2:
                color = "#801A9BFF";
                break;
            case 3:
                color = "#9602FC04";
                break;
            case 4:
                color = "#804D4D4D";
                break;
        }
        return Color.parseColor(color);
    }

    //获取颜色 - 手动添加
    public static int getCircleColor_Custom(Context context) {
        int index = SPUtil.getIntValue(context, SP_CircleColor_Custom, 0);
        String color = "#96EA2100";
        switch (index) {
            case 0:
                color = "#96EA2100";
                break;
            case 1:
                color = "#80FFBE00";
                break;
            case 2:
                color = "#801A9BFF";
                break;
            case 3:
                color = "#8028B227";
                break;
            case 4:
                color = "#804D4D4D";
                break;
        }
        return Color.parseColor(color);
    }

    //设置颜色
    public static void setCircleColor(Context context, int color) {
        SPUtil.putValue(context, SP_CircleColor, color);
    }

    //获取图标样式   0：圆形   1：加号
    public static int getCircleStyle(Context context) {
        return SPUtil.getIntValue(context, SP_CircleStyle, 0);
    }

    //设置图标样式
    public static void setCircleStyle(Context context, int value) {
        SPUtil.putValue(context, SP_CircleStyle, value);
    }

    //设置矩形框背景颜色透明度    0 ~ 255
    public static void setFrame_BgColor_Alpha(Context context, int alpha) {
        SPUtil.putValue(context, SP_Frame_BgColor, alpha);
    }

    //设置矩形框背景颜色透明度    0 ~ 255
    public static int getFrame_BgColor_Alpha(Context context) {
        return SPUtil.getIntValue(context, SP_Frame_BgColor, 150);
    }

    //获取矩形框背景颜色
    public static int getFrame_BgColor(Context context) {
        int alpha = getFrame_BgColor_Alpha(context);
        int color = Color.argb(alpha, 0, 0, 0);
        return color;
    }

    //获取显示/隐藏 标记
    public static int getShowIndex(Context context) {
        return SPUtil.getIntValue(context, SP_Show_index, 0);
    }

    //设置显示/隐藏 标记
    public static void setShowIndex(Context context, int value) {
        SPUtil.putValue(context, SP_Show_index, value);
    }

    //设置显示/隐藏 标记
    public static void addShowIndex(Context context) {
        int show_index = getShowIndex(context);
        show_index++;
        if (show_index == 7) show_index = 0;
        setShowIndex(context, show_index);
    }


}
