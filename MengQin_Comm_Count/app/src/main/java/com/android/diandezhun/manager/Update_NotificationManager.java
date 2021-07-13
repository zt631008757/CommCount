package com.android.diandezhun.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.diandezhun.R;


/**
 * Created by Administrator on 2019/1/25.
 */

public class Update_NotificationManager {

    static int id = 2000;

    /***
     * 创建通知栏
     */
    public void createNotification(Context mContext, String title, String content, String url) {
        id++;
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "channel_first";
        //这里必须设置chanenelId,要不然该通知在8.0手机上，不能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelId(manager,channelId);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)//用户点击就自动消失
                .setSmallIcon(R.drawable.ico_logo)
                .setContentIntent(getPendingIntent(mContext,url))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
                .setDefaults(Notification.DEFAULT_SOUND);    //设置系统的提示音
//                .setVisibility(Notification.VISIBILITY_PUBLIC)
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
        manager.notify(id, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannelId(NotificationManager manager, String channelId) {
        String group_primary = "group_second";
        NotificationChannelGroup ncp1 = new NotificationChannelGroup(group_primary, "通知渠道组2");
        manager.createNotificationChannelGroup(ncp1);
        NotificationChannel chan = new NotificationChannel(channelId, "通知渠道2", NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.GREEN);
        chan.setGroup(group_primary);
        //锁屏的时候是否展示通知
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        manager.createNotificationChannel(chan);
    }

    /**
     * 通知栏点击回调事件
     */
    private PendingIntent getPendingIntent(Context mContext, String url) {
//        Intent intent = new Intent(mContext, NotificationClickReceiver.class);
//        intent.putExtra("androidUrl", url);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        return pendingIntent;
        return null;
    }

}
