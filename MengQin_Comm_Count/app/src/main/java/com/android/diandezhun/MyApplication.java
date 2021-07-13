package com.android.diandezhun;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.multidex.MultiDex;
import com.android.baselibrary.BaseLib;
import com.android.baselibrary.tool.Log;
import com.android.diandezhun.manager.SqlHelper;

import org.litepal.LitePal;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/8/13.
 */

public class MyApplication extends Application {

    public static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        if (isRepeat()) return;
        Log.i("APP MyApplication加载 ");
        BaseLib.init(context);

        LitePal.initialize(this);
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);

    }

    //如果APP启用了远程的service，此application:onCreate会被调用2次 检查重复
    private boolean isRepeat() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(context.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return true;
        }
        return false;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

}
