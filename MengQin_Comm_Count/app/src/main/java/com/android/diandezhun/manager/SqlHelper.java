package com.android.diandezhun.manager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.android.diandezhun.bean.Count_DetailInfo;

import org.litepal.LitePal;

public class SqlHelper {

    //将历史记录所有的选择状态重置
    public static void resetHistorySelectState() {
        try {
            ContentValues values = new ContentValues();
            values.put("isSelect", "0");
            LitePal.getDatabase().update("Count_DetailInfo", values, " isSelect=?", new String[]{"1"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将历史记录所有的选择状态重置
    public static void resetFormSelectState() {
        try {
            ContentValues values = new ContentValues();
            values.put("isSelect", "0");
            LitePal.getDatabase().update("Count_FormRecord", values, " isSelect=?", new String[]{"1"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
