package com.android.diandezhun.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 出库单
 */
public class Count_FormRecord  extends LitePalSupport implements Serializable {

    @Column(unique = true, defaultValue = "unknown")
    public int id;
    public int inAndOutType; //出入库   0：出库  1：入库
    public long editTime = System.currentTimeMillis();   //最后修改时间
    public String company = "";  //公司名
    public String countDetailList;   //Gson字符

    public int isSelect;
}
