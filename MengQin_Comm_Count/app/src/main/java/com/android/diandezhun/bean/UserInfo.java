package com.android.diandezhun.bean;

import java.io.Serializable;

/**
 * 用户信息
 */

public class UserInfo implements Serializable {

    public String userId;// ":2,"
    public String phone; //":" 18672782213
    public String inviteCode; //   邀请码

    public double totalBalance = 0.0;   //余额
    public double earnedToday = 0.0;    //今日已赚金币
    public int totalFriends;            //好友数量


}
