package com.android.diandezhun.bean;

import java.io.Serializable;

public class History_FillterInfo implements Serializable {

    public long startTime;
    public long endTime;
    public int inAndOutType; //出入库   0：出库  1：入库
    public String company;
}
